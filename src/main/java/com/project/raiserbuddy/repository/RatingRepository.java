package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
	
	@Query("Select r From Rating r where r.product.productId=:productId")
    List<Rating> getAllProductsRating(@Param("productId") Integer productId);

}
