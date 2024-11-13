package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.ProdDTO;
import com.project.raiserbuddy.dto.WishDTO;
import com.project.raiserbuddy.dto.WishlistDTO;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.Wishlist;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.repository.ProductRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import com.project.raiserbuddy.repository.WishlistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsersRepository userRepo;

    @Autowired
    private ProductRepository productRepository;

//        public WishlistDTO getWishlistByUserId(Integer userId){
//            WishlistDTO resp = new WishlistDTO();
//            List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
//
//            List<WishDTO> wishlistDtos = wishlists.stream().map(wishlist -> modelMapper.map(wishlist, WishDTO.class))
//                    .toList();
//            resp.setMessage("Product added to wishlist Successfully");
//            resp.setStatusCode(201);
//            resp.setWishlists(wishlistDtos);
//            return resp;
//    }

    public List<ProdDTO> getWishlist(Integer userId) {

        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        List<Product> products = new ArrayList<>();
        for (Wishlist wishlist : wishlists) {
            products.addAll(wishlist.getProducts());
        }

        List<ProdDTO> productDtos = products.stream().map(product -> modelMapper.map(product, ProdDTO.class)).toList();
        return productDtos;
    }

    public WishlistDTO addProductToWishlist(Integer userId, Integer productId) throws ProductException {
        WishlistDTO resp = new WishlistDTO();
        Wishlist wishlist = new Wishlist();
        OurUsers user = userRepo.findById(userId).orElseThrow();
        wishlist.setUser(user);
        if (wishlist.getProducts() == null) {
            wishlist.setProducts(new HashSet<>());
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        wishlist.getProducts().add(product);

        Wishlist existwishlist = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existwishlist != null) {
            throw new APIException("wishlist with product already exists !!!", 409);
        }

        existwishlist = wishlistRepository.save(wishlist);
        resp.setMessage("Product added to wishlist Successfully");
        resp.setStatusCode(201);
        modelMapper.map(wishlist, resp);

        return resp;
    }

    public WishlistDTO removeFromWishlist(Integer productId, Integer userId) {
        WishlistDTO wishlistDTO= new WishlistDTO();
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        for (Wishlist wishlist : wishlists) {
        if (wishlist != null && wishlist.getProducts() != null) {
            wishlist.getProducts().removeIf(product -> product.getProductId().equals(productId));
            wishlistRepository.save(wishlist);
            wishlistDTO.setStatusCode(200);
            wishlistDTO.setMessage("Wishlist Item Removed successfully");
        }
        }

        return wishlistDTO;
    }

}