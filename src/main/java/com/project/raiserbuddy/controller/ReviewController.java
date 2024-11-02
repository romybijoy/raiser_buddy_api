package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.ReviewDTO;
import com.project.raiserbuddy.dto.ReviewRequest;
import com.project.raiserbuddy.dto.ReviewResponse;
import com.project.raiserbuddy.entity.Reviews;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.service.ReviewService;
import com.project.raiserbuddy.service.UsersManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UsersManagementService userService;
    @PostMapping("/create/{email}")
    public ResponseEntity<Reviews> createReviewHandler(@RequestBody ReviewRequest req, @PathVariable String email) throws UserException, ProductException {
        System.out.println("product id "+req.getProductId()+" - "+req.getReview());
        Reviews review=reviewService.createReview(req, email);
        System.out.println("product review "+req.getReview());
        return new ResponseEntity<Reviews>(review,HttpStatus.ACCEPTED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Reviews>> getProductsReviewHandler(@PathVariable Integer productId){
        List<Reviews>reviews=reviewService.getAllReview(productId);
        return new ResponseEntity<List<Reviews>>(reviews,HttpStatus.OK);
    }

}
