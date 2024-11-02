package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
@Entity
@Table(name= "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_item_id")
    private Integer cartItemId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart", referencedColumnName = "cart_id")
    @JsonBackReference("cartRef")
    private Cart cart;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prod_id")
    private Product product;

    private Integer quantity;
    private double discountedPrice;
    private double price;
    private Integer userId;
}
