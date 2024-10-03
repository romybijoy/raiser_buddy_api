package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.APIResponse;
import com.project.raiserbuddy.dto.AddItemRequest;
import com.project.raiserbuddy.dto.CartDTO;
import com.project.raiserbuddy.entity.Cart;
import com.project.raiserbuddy.entity.CartItem;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.repository.UsersRepository;
import com.project.raiserbuddy.service.CartService;
import com.project.raiserbuddy.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

//    @Autowired
//    private CartService cartService;
//
//    @PostMapping("/{cartId}/products/{productId}/quantity/{quantity}")
//    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Integer cartId, @PathVariable Integer productId, @PathVariable Integer quantity) {
//        CartDTO cartDTO = cartService.addProductToCart(cartId, productId, quantity);
//
//        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/")
//    public ResponseEntity<List<CartDTO>> getCarts() {
//
//        List<CartDTO> cartDTOs = cartService.getAllCarts();
//
//        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
//    }
//
//    @GetMapping("/{cartId}/users/{emailId}")
//    public ResponseEntity<CartDTO> getCartById(@PathVariable String emailId, @PathVariable Integer cartId) {
//        CartDTO cartDTO = cartService.getCart(emailId, cartId);
//
//        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
//    }
//
//    @PutMapping("/{cartId}/products/{productId}/quantity/{quantity}")
//    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Integer cartId, @PathVariable Integer productId, @PathVariable Integer quantity) {
//        CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);
//
//        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{cartId}/product/{productId}")
//    public ResponseEntity<String> deleteProductFromCart(@PathVariable Integer cartId, @PathVariable Integer productId) {
//        String status = cartService.deleteProductFromCart(cartId, productId);
//
//        return new ResponseEntity<String>(status, HttpStatus.OK);
//    }
@Autowired
    private CartService cartService;
    @Autowired
    private UsersManagementService userService;

//    public CartController(CartService cartService,UserService userService) {
//        this.cartService=cartService;
//        this.userService=userService;
//    }

    @GetMapping("/{email}")
    public ResponseEntity<Cart> findUserCartHandler( @PathVariable String email) throws UserException{

        Cart cart=cartService.findUserCart(email);

        System.out.println("cart - "+cart.getUser().getEmail());

        return new ResponseEntity<Cart>(cart,HttpStatus.OK);
    }

    @PutMapping("/add/{email}")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                  @PathVariable String email) throws UserException, ProductException {


        CartItem item = cartService.addCartItem(email, req);

        APIResponse res= new APIResponse("Item Added To Cart Successfully",true);

        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);

    }
}