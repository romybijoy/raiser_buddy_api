package com.project.raiserbuddy.controller;


import com.project.raiserbuddy.dto.ProdDTO;
import com.project.raiserbuddy.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{userId}")
    public List<ProdDTO> getWishlist(@PathVariable Integer userId) {
        return wishlistService.getWishlist(userId);
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toggle(@RequestParam Integer userId,
                                    @RequestParam Integer productId) {
        wishlistService.toggleWishlist(userId, productId);
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@RequestParam Integer userId,
                                    @RequestParam Integer productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok("Removed");
    }
}