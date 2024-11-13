package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {


	private Long salesCount;
	private Long usersCount;
	private Long providersCount;
	private Long productCount;

}
