package com.project.raiserbuddy.service;

import com.project.raiserbuddy.entity.Cart;
import com.project.raiserbuddy.entity.CartItem;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.exceptions.CartItemException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.repository.CartItemRepository;
import com.project.raiserbuddy.repository.CartRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UsersManagementService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UsersRepository userRepo;

//    public CartItemServiceImplementation(CartItemRepository cartItemRepository,UserService userService) {
//        this.cartItemRepository=cartItemRepository;
//        this.userService=userService;
//    }


    public CartItem createCartItem(CartItem cartItem) {

        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getSpecialPrice()*cartItem.getQuantity());

        System.out.println("Hi"+cartItem);
        CartItem createdCartItem=cartItemRepository.save(cartItem);

        return createdCartItem;
    }


    public CartItem updateCartItem(String email, Integer id, CartItem cartItem) throws CartItemException, UserException {


        OurUsers givenUser = userRepo.findByEmail(email).orElseThrow();
        CartItem item=findCartItemById(id);
        OurUsers user=userService.findUserById(item.getUserId());

        if(user.getId().equals(givenUser.getId())) {

            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity()*item.getProduct().getPrice());
            item.setDiscountedPrice(item.getQuantity()*item.getProduct().getSpecialPrice());

            return cartItemRepository.save(item);


        }
        else {
            throw new CartItemException("You can't update  another users cart_item");
        }

    }


    public CartItem isCartItemExist(Cart cart, Product product, Integer userId) {

        CartItem cartItem=cartItemRepository.isCartItemExist(cart, product, userId);

        return cartItem;
    }



    public void removeCartItem(String email,Integer cartItemId) throws CartItemException, UserException {
        OurUsers givenUser = userRepo.findByEmail(email).orElseThrow();

        System.out.println("userId- "+givenUser.getId()+" cartItemId "+cartItemId);

        CartItem cartItem=findCartItemById(cartItemId);

        OurUsers user=userService.findUserById(cartItem.getUserId());
        OurUsers reqUser=userService.findUserById(givenUser.getId());

        if(user.getId().equals(reqUser.getId())) {
            cartItemRepository.deleteById(cartItem.getCartItemId());
        }
        else {
            throw new UserException("you can't remove anothor users item");
        }

    }

    public CartItem findCartItemById(Integer cartItemId) throws CartItemException {
        Optional<CartItem> opt=cartItemRepository.findById(cartItemId);

        if(opt.isPresent()) {
            return opt.get();
        }
        throw new CartItemException("cartItem not found with id : "+cartItemId);
    }

}
