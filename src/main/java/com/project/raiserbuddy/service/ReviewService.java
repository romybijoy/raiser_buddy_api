package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.ReviewDTO;
import com.project.raiserbuddy.dto.ReviewRequest;
import com.project.raiserbuddy.dto.ReviewResponse;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.Reviews;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.exceptions.ResourceNotFoundException;
import com.project.raiserbuddy.repository.ProductRepository;
import com.project.raiserbuddy.repository.ReviewRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    public Reviews createReview(ReviewRequest req, String email) throws ProductException {
        OurUsers user = userRepository.findByEmail(email).orElseThrow();
        Product product=productService.findProductById(req.getProductId());
        Reviews review=new Reviews();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setRating(req.getRating());
        review.setCreatedAt(LocalDateTime.now());

//		product.getReviews().add(review);
        productRepository.save(product);
        return reviewRepository.save(review);
    }

    public List<Reviews> getAllReview(Integer productId) {

        return reviewRepository.getAllProductsReview(productId);
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
