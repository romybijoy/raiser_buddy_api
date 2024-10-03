package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.ReviewDTO;
import com.project.raiserbuddy.dto.ReviewResponse;
import com.project.raiserbuddy.entity.Reviews;
import com.project.raiserbuddy.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<ReviewDTO> create(@Valid @RequestBody Reviews review){
        ReviewDTO response = reviewService.createReview(review);

        return new ResponseEntity<ReviewDTO>(response, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<ReviewResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
                                                            ){

        ReviewResponse reviewResponse = reviewService.getReviews(pageNumber, pageSize);

        return new ResponseEntity<ReviewResponse>(reviewResponse, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getUSerByID(@PathVariable Integer id){
        ReviewDTO response = reviewService.getReviewById(id);
        System.out.println(response);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<ReviewDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer id, @RequestBody Reviews reqres){
        ReviewDTO response = reviewService.updateReview(id, reqres);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<ReviewDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Integer id){
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }

}
