package com.project.raiserbuddy.repository;


import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    List<Coupon> findAll();

    Coupon findByCode(String code);

    @Transactional
    @Query(value = "UPDATE coupon p SET p.status = false WHERE p.id = ?1", nativeQuery = true)
    @Modifying
    void updateStatus(@Param("id") Integer id);

    Page<Coupon> findByStatus(Boolean status, Pageable pageDetails);


}
