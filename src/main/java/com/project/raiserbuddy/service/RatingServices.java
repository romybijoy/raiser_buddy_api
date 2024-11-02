package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.RatingRequest;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.Rating;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.repository.RatingRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServices {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private UsersRepository userRepository;

    public Rating createRating(RatingRequest req, String email) throws ProductException {

        OurUsers user = userRepository.findByEmail(email).orElseThrow();
        Product product = productService.findProductById(req.getProductId());

        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreatedAt(LocalDateTime.now());

        return ratingRepository.save(rating);
    }

    public List<Rating> getProductsRating(Integer productId) {
        return ratingRepository.getAllProductsRating(productId);
    }


}