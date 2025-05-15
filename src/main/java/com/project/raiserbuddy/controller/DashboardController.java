package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.*;
import com.project.raiserbuddy.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/dashboard/chart")
	public SalesReportDTO getDashboard(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		return dashboardService.generateData(startDate, endDate);
	}

	@GetMapping("/dashboard/count")
	public DashboardDTO getDashboard() {
		return dashboardService.getData();
	}

	@GetMapping("/dashboard/monthly")
	public List<SalesMonthDataDTO> getSalesDataByMonth() {
			return dashboardService.getMonthlySalesData();
	}

	@GetMapping("/dashboard/daily")
	public List<SalesDailyDataDTO> getSalesDataByDaily() {
		return dashboardService.getDailySalesData();
	}

	@GetMapping("/dashboard/yearly")
	public List<YearlySalesDataDTO> getSalesDataByYearly() {
		return dashboardService.getYearlySalesData();
	}
}
