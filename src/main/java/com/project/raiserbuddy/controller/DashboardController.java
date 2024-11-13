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
	public List<SalesDataDTO> getSalesDataByMonth(@RequestParam String startDate, @RequestParam String endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		LocalDateTime start = LocalDateTime.parse(startDate, formatter);
		LocalDateTime end = LocalDateTime.parse(endDate, formatter);
		return dashboardService.getSalesDataByMonth(start, end); }

}
