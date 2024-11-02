package com.project.raiserbuddy.repository;


import com.project.raiserbuddy.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
        List<Wishlist> findByUserId(Integer userId);

    @Query("SELECT w FROM Wishlist w JOIN FETCH w.product p WHERE p.productId = :prodId AND w.user.id = :userId")
    Wishlist findByUserIdAndProductId(@Param("userId") Integer userId, @Param("prodId") Integer prodId);
}
