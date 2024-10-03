package com.project.raiserbuddy.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddItemRequest {

	private Integer productId;
	private String size;
	private int quantity;
	private Integer price;
	

}
