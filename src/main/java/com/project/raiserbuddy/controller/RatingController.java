package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.RatingRequest;
import com.project.raiserbuddy.entity.Rating;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.service.RatingServices;
import com.project.raiserbuddy.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {
	@Autowired
	private UsersManagementService userService;
	@Autowired
	private RatingServices ratingServices;

	@PostMapping("/create/{email}")
	public ResponseEntity<Rating> createRatingHandler(@RequestBody RatingRequest req,  @PathVariable String email) throws UserException, ProductException {

		Rating rating=ratingServices.createRating(req, email);
		return new ResponseEntity<>(rating,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Rating>> getProductsReviewHandler(@PathVariable Integer productId){
	
		List<Rating> ratings=ratingServices.getProductsRating(productId);
		return new ResponseEntity<>(ratings,HttpStatus.OK);
	}
}
