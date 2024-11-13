package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.DashboardDTO;
import com.project.raiserbuddy.dto.SalesDataDTO;
import com.project.raiserbuddy.dto.SalesReportDTO;
import com.project.raiserbuddy.entity.Order;
import com.project.raiserbuddy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ProductRepository productRepository;

    public SalesReportDTO generateData(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findOrdersWithinDateRange(startDate, endDate);
        Long salesCount = orderRepository.countOrdersWithinDateRange(startDate, endDate);
        Double totalSales = orderRepository.sumSalesWithinDateRange(startDate, endDate);
        Double totalDiscount = orderRepository.sumDiscountSalesWithinDateRange(startDate, endDate);
        return new SalesReportDTO(orders, salesCount, totalSales,totalDiscount);

    }

    public DashboardDTO getData() {
        Long salesCount = orderRepository.count();
        Long usersCount = usersRepository.count();
        Long providersCount = providerRepository.count();
        Long productsCount = productRepository.count();
        return new DashboardDTO(salesCount, usersCount,providersCount,productsCount);

    }

    public List<SalesDataDTO> getSalesDataByMonth(LocalDateTime startDate, LocalDateTime endDate) {
        return orderItemRepository.getSalesDataByMonth(startDate, endDate); }
}