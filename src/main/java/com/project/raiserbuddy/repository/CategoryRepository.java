package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Category;
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
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM category p WHERE p.status=true AND p.name LIKE %?1%", nativeQuery = true)
    List<Category> search(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM category p WHERE p.status=true", nativeQuery = true)
    Page<Category> findAllByStatus(Pageable pageable);

    Page<Category> findByStatus(Boolean status, Pageable pageDetails);

    @Transactional
    @Query(value = "UPDATE category p SET p.status = false WHERE p.category_id = ?1", nativeQuery = true)
    @Modifying
    void updateStatus(@Param("category_id") Integer category_id);

    Category findByName(String name);

    Page<Category> findByNameIgnoreCaseContainingAndStatus(String keyword, boolean status, Pageable pageDetails);
}
