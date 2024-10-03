package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.APIResponse;
import com.project.raiserbuddy.entity.CartItem;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.exceptions.CartItemException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.repository.UsersRepository;
import com.project.raiserbuddy.service.CartItemService;
import com.project.raiserbuddy.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart_items")
public class CartItemController {

	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private UsersManagementService userService;
	
	
	@DeleteMapping("/{email}/{cartItemId}")
	public ResponseEntity<APIResponse>deleteCartItemHandler(@PathVariable Integer cartItemId, @PathVariable String email) throws CartItemException, UserException {

		cartItemService.removeCartItem(email, cartItemId);
		
		APIResponse res=new APIResponse("Item Remove From Cart",true);
		
		return new ResponseEntity<APIResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{email}/{cartItemId}")
	public ResponseEntity<CartItem>updateCartItemHandler(@PathVariable String email, @PathVariable Integer cartItemId, @RequestBody CartItem cartItem) throws CartItemException, UserException{


		System.out.println("hello");
		CartItem updatedCartItem =cartItemService.updateCartItem(email, cartItemId, cartItem);
		
		//APIResponse res=new APIResponse("Item Updated",true);
		
		return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
	}
}
