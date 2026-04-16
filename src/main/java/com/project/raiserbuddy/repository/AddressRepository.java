package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByUserId(Integer userId);

}
