package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, Integer> {

    @Query("Select r from Reviews r where r.product.id=:productId")
    List<Reviews> getAllProductsReview(@Param("productId") Integer productId);


}
