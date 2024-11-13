package com.project.raiserbuddy.repository;

import com.project.raiserbuddy.dto.SalesDataDTO;
import com.project.raiserbuddy.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.orderStatus = PENDING OR o.orderStatus = PLACED OR o.orderStatus = CONFIRMED OR o.orderStatus = SHIPPED OR o.orderStatus = DELIVERED OR o.orderStatus = CANCELLED)")
	public List<Order> getUsersOrders(@Param("userId") Integer userId);
	
	List<Order> findAllByOrderByCreatedAtDesc();

	@Query("SELECT o FROM Order o JOIN FETCH o.shippingAddress")
	Page<Order> findAllOrdersWithShippingAddress(Pageable pageDetails);

	@Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")

	List<Order> findOrdersWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
	Long countOrdersWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);

	@Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
	Double sumSalesWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);

	@Query("SELECT SUM(o.discount) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
	Double sumDiscountSalesWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);

	@Query("SELECT COUNT(o) FROM Order o")
	Long countOrders();


}
