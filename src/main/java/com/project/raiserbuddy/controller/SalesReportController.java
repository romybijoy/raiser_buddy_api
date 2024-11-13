package com.project.raiserbuddy.controller;


import com.project.raiserbuddy.dto.SalesReportDTO;
import com.project.raiserbuddy.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sales")
public class SalesReportController {

    @Autowired
    private SalesReportService salesReportService;

    @GetMapping("/report")
    public SalesReportDTO getSalesReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return salesReportService.generateSalesReport(startDate, endDate);
    }
}
