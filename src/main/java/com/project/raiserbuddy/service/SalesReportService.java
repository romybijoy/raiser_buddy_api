package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.SalesReportDTO;
import com.project.raiserbuddy.entity.Order;
import com.project.raiserbuddy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SalesReportService {

    @Autowired
    private OrderRepository orderRepository;


    public SalesReportDTO generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findOrdersWithinDateRange(startDate, endDate);
        Long salesCount = orderRepository.countOrdersWithinDateRange(startDate, endDate);
        Double totalSales = orderRepository.sumSalesWithinDateRange(startDate, endDate);
        Double totalDiscount = orderRepository.sumDiscountSalesWithinDateRange(startDate, endDate);
        return new SalesReportDTO(orders, salesCount, totalSales,totalDiscount);

    }

}