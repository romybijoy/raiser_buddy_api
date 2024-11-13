package com.project.raiserbuddy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String customerName;

	@Column(nullable = false)
	private Date invoiceDate;

	@Column(nullable = false)
	private Date dueDate;

	private double amount;

	@Column(nullable = false)
	private String status;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "invoice_id")
//	private List<OrderItem> items;


}