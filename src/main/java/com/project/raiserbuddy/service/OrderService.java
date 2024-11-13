package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.OrderDTO;
import com.project.raiserbuddy.dto.OrderResponse;
import com.project.raiserbuddy.entity.*;
import com.project.raiserbuddy.enums.OrderStatus;
import com.project.raiserbuddy.enums.PaymentStatus;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.OrderException;
import com.project.raiserbuddy.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UsersRepository userRepository;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	public ModelMapper modelMapper;

	public Order createOrder(String email, Address shippAddress) {

		OurUsers user = userRepository.findByEmail(email).orElseThrow();
		shippAddress.setUser(user);
		Address address;

		userRepository.save(user);

		if(shippAddress.getAdd_id() != null){
			address= shippAddress;
		}
		else{
			address= addressRepository.save(shippAddress);
			user.getAddresses().add(address);
		}
		Cart cart=cartService.findUserCart(user.getEmail());
		List<OrderItem> orderItems=new ArrayList<>();
		
		for(CartItem item: cart.getCartItems()) {
			OrderItem orderItem=new OrderItem();
			
			orderItem.setPrice(item.getPrice());
// start product deduction and sales update
			Optional<Product> optionalProduct = productRepository.findById(item.getProduct().getProductId());
			if (optionalProduct.isPresent()) {
				Product product = optionalProduct.get();
				if (product.getQuantity() >= item.getQuantity()) {
					product.setQuantity(product.getQuantity() - item.getQuantity());
					product.setSales(product.getSales() + 1);
					productRepository.save(product);

					orderItem.setProduct(item.getProduct());

					orderItem.setQuantity(item.getQuantity());
					orderItem.setUserId(item.getUserId());
					orderItem.setDiscountedPrice(item.getDiscountedPrice());


					OrderItem createdOrderItem = orderItemRepository.save(orderItem);

					orderItems.add(createdOrderItem);
				}
				}// end product deduction and sales update
		}

		Order createdOrder=new Order();
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setDiscount(cart.getDiscount());
		createdOrder.setTotalItem(cart.getTotalItem());

		System.out.println(address);
		createdOrder.setShippingAddress(address);
		createdOrder.setOrderDate(LocalDateTime.now());
		createdOrder.setOrderStatus(OrderStatus.PENDING);
		createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
		createdOrder.setCreatedAt(LocalDateTime.now());
		
		Order savedOrder=orderRepository.save(createdOrder);
		
		for(OrderItem item:orderItems) {
			item.setOrder(savedOrder);
			orderItemRepository.save(item);
		}

		return savedOrder;
		
	}


	public Address createAddress(String email, Address shippAddress) {

		OurUsers user = userRepository.findByEmail(email).orElseThrow();
		shippAddress.setUser(user);

		userRepository.save(user);

		Address address= addressRepository.save(shippAddress);
			user.getAddresses().add(address);


		return address;

	}

	@Transactional
	public Order placedOrder(Integer orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.PLACED);
		order.setOrderId(UUID.randomUUID().toString());
		Integer userId = order.getUser().getId();
		order.getOrderItems().forEach((item) -> {
			Cart cart = cartRepository.findByUserId(userId);
			System.out.println(item.getProduct().getProductId());

			System.out.println(cart.getCartId());
			cartItemRepository.deleteCartItemByProductIdAndCartId(cart.getCartId(),item.getProduct().getProductId());
		});

		return order;
	}



	public Order confirmedOrder(Integer orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CONFIRMED);
		
		
		return orderRepository.save(order);
	}

	public Order shippedOrder(Integer orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.SHIPPED);
		return orderRepository.save(order);
	}


	public Order deliveredOrder(Integer orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.DELIVERED);
		return orderRepository.save(order);
	}


	public Order cancledOrder(Integer orderId, Integer userId) throws OrderException {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		OurUsers user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// Update wallet balance
		Double balance = user.getWalletBalance() + order.getTotalDiscountedPrice();
		user.setWalletBalance(balance);
		userRepository.save(user);
		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}


	public Order findOrderById(Integer orderId) throws OrderException {
		Optional<Order> opt=orderRepository.findById(orderId);
		
		if(opt.isPresent()) {

			System.out.println(opt.get().getShippingAddress());
			return opt.get();
		}
		throw new OrderException("order not exist with id "+orderId);
	}


	public List<OrderDTO> usersOrderHistory(String email) {
		OurUsers givenUser = userRepository.findByEmail(email).orElseThrow();
		List<Order> orders=orderRepository.getUsersOrders(givenUser.getId());

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.toList();
		return orderDTOs;
	}


//	public List<Order> getAllOrders() {
//		
//		return orderRepository.findAllByOrderByCreatedAtDesc();
//	}

	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);


		Page<Order> pageOrders;
		pageOrders = orderRepository.findAllOrdersWithShippingAddress(pageDetails);

		List<Order> orders = pageOrders.getContent();


		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
//
//
//		(List<OrderDTO>) orders.stream().map(order -> new OrderDTO(
//				order.getId(),
//				order.getOrderItems(),
//				order.getUser(),
//				order.getOrderDate(),
//				order.getDiscount(),
//				order.getDeliveryDate(),
//				order.getOrderStatus(),
//				order.getTotalDiscountedPrice(),
//				order.getTotalPrice(),
//				order.getTotalItem(),
//				new AddressDTO(
//						order.getShippingAddress().getAdd_id(),
//						order.getShippingAddress().getHouse_name(),
//						order.getShippingAddress().getPlace(),
//						order.getShippingAddress().getState(),
//						order.getShippingAddress().getDistrict(),
//						order.getShippingAddress().getCountry(),
//						order.getShippingAddress().getMobile()
//				)
//		));

		if (orders.isEmpty()) {
			throw new APIException("No order is created till now", 404);
		}

//        List<Order> orderDTOs = categories.stream().collect(Collectors.toList());

		OrderResponse orderResponse = new OrderResponse();

		orderResponse.setMessage("Order fetched Successfully");
		orderResponse.setStatusCode(302);
		orderResponse.setContent(orderDTOs);
		orderResponse.setPageNumber(pageOrders.getNumber());
		orderResponse.setPageSize(pageOrders.getSize());
		orderResponse.setTotalElements(pageOrders.getTotalElements());
		orderResponse.setTotalPages(pageOrders.getTotalPages());
		orderResponse.setLastPage(pageOrders.isLast());

		return orderResponse;
	}
	
	public void deleteOrder(Integer orderId) throws OrderException {
		Order order =findOrderById(orderId);
		
		orderRepository.deleteById(orderId);
		
	}

}
