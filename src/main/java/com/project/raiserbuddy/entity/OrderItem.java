package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order",  referencedColumnName = "id")
	@JsonBackReference("itemRef")
	private Order order;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product",  referencedColumnName = "prod_id")
	private Product product;
	
	private String size;
	
	private int quantity;
	
	private double price;
	
	private double discountedPrice;
	
	private Integer userId;
	
	private LocalDateTime deliveryDate;
	


}
