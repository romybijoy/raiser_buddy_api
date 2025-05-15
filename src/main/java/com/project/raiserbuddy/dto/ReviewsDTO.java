package com.project.raiserbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsDTO {

	private Integer productId;
	private String review;
	private double rating;
	private UserDTO user;
	private LocalDateTime createdAt;
}
