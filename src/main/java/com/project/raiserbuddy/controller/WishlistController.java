package com.project.raiserbuddy.controller;


import com.project.raiserbuddy.dto.CategoryDTO;
import com.project.raiserbuddy.dto.WishlistDTO;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.Wishlist;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{userId}")
    public WishlistDTO getWishlist(@PathVariable Integer userId) {
        return wishlistService.getWishlistByUserId(userId);
    }

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<WishlistDTO> addProductToWishlist(@PathVariable Integer userId, @PathVariable Integer productId) throws ProductException {
        WishlistDTO response =  wishlistService.addProductToWishlist(userId, productId);
        return new ResponseEntity<WishlistDTO>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/{productId}")
    public void removeProductFromWishlist(@PathVariable Integer userId, @PathVariable Integer productId) {
        wishlistService.removeProductFromWishlist(userId, productId);
    }
}
