package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    Optional<Provider> findByEmail(String email);

    @Transactional
    @Query(value = "UPDATE provider p SET p.status = false WHERE p.id = ?2", nativeQuery = true)
    @Modifying
    void updateBlockInfo(@Param("userId") Integer userId);

    @Query(value = "SELECT * FROM provider p WHERE p.status=true", nativeQuery = true)
    List<Provider> findAllByStatus();

    Page<Provider> findByStatus(Boolean status, Pageable pageDetails);

    Page<Provider> findByNameOrEmailIgnoreCaseContainingAndStatus(String keyword, String email, Boolean status, Pageable pageDetails);

    @Query(value="SELECT COUNT(*) FROM provider p WHERE p.status=true", nativeQuery = true)
    Long countProviders();

}
