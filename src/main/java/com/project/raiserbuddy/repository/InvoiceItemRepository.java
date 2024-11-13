package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {


}
