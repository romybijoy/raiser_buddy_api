package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.dto.SalesDataDTO;
import com.project.raiserbuddy.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT new com.project.raiserbuddy.dto.SalesDataDTO(CONCAT(YEAR(o.orderDate), '-', " + "CASE WHEN MONTH(o.orderDate) < 10 THEN CONCAT('0', MONTH(o.orderDate)) ELSE CAST(MONTH(o.orderDate) AS string) END), " + "SUM(oi.quantity)) " + "FROM OrderItem oi JOIN oi.order o " + "WHERE o.orderDate BETWEEN :startDate AND :endDate " + "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " + "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)")
    List<SalesDataDTO> getSalesDataByMonth(LocalDateTime startDate, LocalDateTime endDate);

}
