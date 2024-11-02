package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportDTO {

	private List<Order> orders;
	private Long salesCount;
	private Double totalSales;
	private Double totalDiscount;
	
}
