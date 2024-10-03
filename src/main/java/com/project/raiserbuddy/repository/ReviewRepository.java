package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Reviews, Integer> {
}
