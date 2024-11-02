package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.ProdDTO;
import com.project.raiserbuddy.dto.UserDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        public WishlistDTO getWishlistByUserId(Integer userId){
            WishlistDTO resp = new WishlistDTO();
            List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);

            List<WishDTO> wishlistDtos = wishlists.stream().map(wishlist -> modelMapper.map(wishlist, WishDTO.class))
                    .toList();
            resp.setMessage("Product added to wishlist Successfully");
            resp.setStatusCode(201);
            resp.setWishlists(wishlistDtos);
            return resp;
    }

        public WishlistDTO addProductToWishlist(Integer userId, Integer productId) throws ProductException {
        WishlistDTO resp = new WishlistDTO();
            Wishlist wishlist = new Wishlist();
        OurUsers user = userRepo.findById(userId).orElseThrow();
        wishlist.setUser(user);
        Product product=productService.findProductById(productId);
        wishlist.setProduct(product);

        Wishlist existwishlist = wishlistRepository.findByUserIdAndProductId (userId, productId);
        if(existwishlist != null) {
            throw new APIException("wishlist with product already exists !!!", 409);
        }

            existwishlist= wishlistRepository.save(wishlist);
            resp.setMessage("Product added to wishlist Successfully");
            resp.setStatusCode(201);
            modelMapper.map(wishlist, resp);

            return resp;
    }

        public void removeProductFromWishlist (Integer userId, Integer productId){
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId (userId, productId);
        wishlistRepository.delete(wishlist);
    }
    }