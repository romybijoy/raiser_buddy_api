package com.project.raiserbuddy.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.raiserbuddy.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "order_id")
	private String orderId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonBackReference("userOrderRef")
	private OurUsers user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("itemRef")
	private List<OrderItem> orderItems = new ArrayList<>();

	private LocalDateTime orderDate;

	private LocalDateTime deliveryDate;


	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "shipping_address",  referencedColumnName = "add_id")
	@JsonBackReference("ordAddRef")
	private Address shippingAddress;

    @Embedded
    private PaymentDetails paymentDetails=new PaymentDetails();

	private double totalPrice;

	private double totalDiscountedPrice;

	private double discount;

	private OrderStatus orderStatus;

	private int totalItem;

	private LocalDateTime createdAt;


}