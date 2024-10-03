package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM product p WHERE p.name LIKE %?1%", nativeQuery = true)
    List<Product> search(@Param("keyword") String keyword);

    @Transactional
    @Query(value = "UPDATE product p SET p.status = false WHERE p.prod_id = ?1", nativeQuery = true)
    @Modifying
    void updateStatus(@Param("prod_id") Integer prod_id);

    @Query(value = "SELECT * FROM product p WHERE p.status=true", nativeQuery = true)
    List<Product> findAllByStatus();

    Page<Product> findByStatus(Boolean status, Pageable pageDetails);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = ?1 AND p.status=true")
    List<Product> findProductByCategoryId(Integer categoryId);

    Product findByName(String name);

    List<Product> findByStatus(boolean status);

    Page<Product> findByNameIgnoreCaseContainingAndStatus(String keyword, boolean status, Pageable pageDetails);

}
