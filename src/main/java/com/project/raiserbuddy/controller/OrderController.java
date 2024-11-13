package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.OrderDTO;
import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.Order;
import com.project.raiserbuddy.exceptions.OrderException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.service.OrderService;
import com.project.raiserbuddy.service.UsersManagementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UsersManagementService userService;

	@Autowired
	public ModelMapper modelMapper;

	@PostMapping("/{email}")
	public ResponseEntity<Order> createOrderHandler(@RequestBody Address spippingAddress,
													@PathVariable String email) throws UserException{
		
		Order order =orderService.createOrder(email, spippingAddress);
		
		return new ResponseEntity<Order>(order,HttpStatus.OK);
		
	}


	@PostMapping("/address/{email}")
	public ResponseEntity<Address> createAddress(@RequestBody Address spippingAddress,
													@PathVariable String email) throws UserException{

		Address address =orderService.createAddress(email, spippingAddress);

		return new ResponseEntity<Address>(address,HttpStatus.OK);

	}
	
	@GetMapping("/user/{email}")
	public ResponseEntity< List<OrderDTO>> usersOrderHistoryHandler( @PathVariable String email) throws OrderException, UserException {
		List<OrderDTO> orders=orderService.usersOrderHistory(email);
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> findOrderHandler(@PathVariable Integer orderId) throws OrderException, UserException{

		Order order=orderService.findOrderById(orderId);

			OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

		return new ResponseEntity<>(orderDTO,HttpStatus.ACCEPTED);
	}

	@PutMapping("/cod/{orderId}")
	public ResponseEntity<Order> codOrderHandler(@PathVariable Integer orderId,
													   @RequestHeader("Authorization") String jwt) throws OrderException {
		Order order=orderService.placedOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
	}

	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<Order> canceledOrderHandler(@PathVariable Integer orderId,
													  @RequestParam(name="user_id") Integer userId) throws OrderException{
		Order order=orderService.cancledOrder(orderId, userId);
		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
	}

}
