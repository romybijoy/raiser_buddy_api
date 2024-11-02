package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.APIResponse;
import com.project.raiserbuddy.dto.OrderResponse;
import com.project.raiserbuddy.entity.Order;
import com.project.raiserbuddy.exceptions.OrderException;
import com.project.raiserbuddy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

	@Autowired
	private OrderService orderService;

//	public AdminOrderController(OrderService orderService) {
//		this.orderService=orderService;
//	}

//	@GetMapping("/")
//	public ResponseEntity<List<Order>> getAllOrdersHandler(){
//		List<Order> orders=orderService.getAllOrders();
//
//		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
//	}

	@GetMapping()
	public ResponseEntity<OrderResponse> getAllOrders(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
													  @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
													  @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDER_BY, required = false) String sortBy,
													  @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){

		OrderResponse productResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

		return new ResponseEntity<OrderResponse>(productResponse, HttpStatus.FOUND);
	}
	
	@PutMapping("/{orderId}/confirmed")
	public ResponseEntity<Order> ConfirmedOrderHandler(@PathVariable Integer orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException {
		Order order=orderService.confirmedOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{orderId}/ship")
	public ResponseEntity<Order> shippedOrderHandler(@PathVariable Integer orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		Order order=orderService.shippedOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{orderId}/deliver")
	public ResponseEntity<Order> deliveredOrderHandler(@PathVariable Integer orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		Order order=orderService.deliveredOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
	}
	
//	@PutMapping("/{orderId}/cancel")
//	public ResponseEntity<Order> canceledOrderHandler(@PathVariable Integer orderId,
//			@RequestHeader("Authorization") String jwt) throws OrderException{
//		Order order=orderService.cancledOrder(orderId);
//		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
//	}
	
	@DeleteMapping("/{orderId}/delete")
	public ResponseEntity<APIResponse> deleteOrderHandler(@PathVariable Integer orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException{
		orderService.deleteOrder(orderId);
		APIResponse res=new APIResponse("Order Deleted Successfully",true);
		System.out.println("delete method working....");
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}

}
