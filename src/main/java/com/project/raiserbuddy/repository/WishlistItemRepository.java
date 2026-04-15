package com.project.raiserbuddy.repository;


import com.project.raiserbuddy.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Integer> {

    List<WishlistItem> findByUserId(Integer userId);

    Optional<WishlistItem> findByUserIdAndProductProductId(Integer userId, Integer productId);

    void deleteByUserIdAndProductProductId(Integer userId, Integer productId);


}
