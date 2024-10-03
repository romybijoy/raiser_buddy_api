package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.ReviewDTO;
import com.project.raiserbuddy.dto.ReviewResponse;
import com.project.raiserbuddy.entity.Reviews;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.ResourceNotFoundException;
import com.project.raiserbuddy.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ModelMapper modelMapper;



    public ReviewDTO  createReview(Reviews review){
        ReviewDTO resp = new ReviewDTO();
        Reviews savedReview = reviewRepository.save(review);

        resp.setMessage("Review Saved Successfully");
        resp.setStatusCode(201);
        modelMapper.map(savedReview, resp);

        return resp;
    }


    public ReviewResponse getReviews(Integer pageNumber, Integer pageSize) {

//        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
//                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);

        Page<Reviews> pageCategories;
        pageCategories = reviewRepository.findAll(pageDetails);

        List<Reviews> categories = pageCategories.getContent();

        if (categories.isEmpty()) {
            throw new APIException("No review is created till now", 404);
        }

        ReviewResponse reviewResponse = getReviewResponse(categories, pageCategories);

        return reviewResponse;
    }

    private static ReviewResponse getReviewResponse(List<Reviews> categories, Page<Reviews> pageCategories) {
        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.setMessage("Review fetched Successfully");
        reviewResponse.setStatusCode(302);
        reviewResponse.setContent(categories);
        reviewResponse.setPageNumber(pageCategories.getNumber());
        reviewResponse.setPageSize(pageCategories.getSize());
        reviewResponse.setTotalElements(pageCategories.getTotalElements());
        reviewResponse.setTotalPages(pageCategories.getTotalPages());
        reviewResponse.setLastPage(pageCategories.isLast());
        return reviewResponse;
    }

    public ReviewDTO getReviewById(Integer review_id) {
        ReviewDTO reviewDTO = new ReviewDTO();
        try {
            Reviews reviewById = reviewRepository.findById(review_id).orElseThrow(() -> new RuntimeException("Review Not found"));
            reviewDTO.setReview(reviewById);
            reviewDTO.setStatusCode(200);
            reviewDTO.setMessage("Review with id '" + review_id + "' found successfully");
        } catch (Exception e) {
            reviewDTO.setStatusCode(500);
            reviewDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return reviewDTO;
    }


    public ReviewDTO deleteReview(Integer id) {
        ReviewDTO reviewDTO = new ReviewDTO();
        Reviews review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", id, 404));

        reviewRepository.deleteById(id);
        reviewDTO.setStatusCode(200);
        reviewDTO.setMessage("Review deleted successfully");

        return reviewDTO;
    }


    public ReviewDTO updateReview(Integer reviewId, Reviews updatedReview) {

        ReviewDTO resp = new ReviewDTO();

        Reviews savedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId, 404));

        updatedReview.setReview_id(reviewId);

        savedReview = reviewRepository.save(updatedReview);

        resp.setMessage("Review Saved Successfully");
        resp.setStatusCode(201);
        modelMapper.map(savedReview, resp);
        return resp;
    }
}
