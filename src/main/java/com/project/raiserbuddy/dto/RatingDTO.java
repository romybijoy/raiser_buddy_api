package com.project.raiserbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

	private Integer id;

	private UserDTO user;

	private double rating;

	private LocalDateTime createdAt;
	
	
}
