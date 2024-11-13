package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.OurUsers;
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
public interface UsersRepository extends JpaRepository<OurUsers, Integer> {
    Optional<OurUsers> findByEmail(String email);

    @Query(value = "SELECT * FROM ourusers p WHERE p.name LIKE %?1% OR p.email LIKE %?1% OR p.role=?2", nativeQuery = true)
    List<OurUsers> search(@Param("keyword") String keyword, @Param("role") String role);

    List<OurUsers> findByRoleContaining(String Role);

    @Transactional
    @Query(value = "UPDATE ourusers p SET p.block_reason = ?1, p.enabled = false WHERE p.id = ?2", nativeQuery = true)
    @Modifying
    void updateBlockInfo(@Param("block_reason") String block_reason, @Param("userId") Integer userId);

    @Query(value = "SELECT * FROM ourusers p WHERE p.enabled=true", nativeQuery = true)
    List<OurUsers> findAllByStatus();

    Page<OurUsers> findByEnabled(Boolean enabled, Pageable pageDetails);



    Page<OurUsers> findByNameOrEmailIgnoreCaseContainingAndEnabled(String keyword, String email, Boolean enabled, Pageable pageDetails);

    @Query(value="SELECT COUNT(*) FROM ourusers o WHERE o.enabled=true", nativeQuery = true)
    Long countUsers();
}
