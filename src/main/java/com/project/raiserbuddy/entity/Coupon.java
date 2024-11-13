package com.project.raiserbuddy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private String title;

    private String desc;

    private double discount; // Could be percentage or fixed amount

//    private String discountType; // 'PERCENTAGE' or 'FIXED'

    @Temporal(TemporalType.DATE)
    private Date validFrom;

    @Temporal(TemporalType.DATE)
    private Date validUntil;

    private boolean status; // To check if the coupon is active

    private Long userId; // User-specific coupon

    private Long maxRedemptions; // Maximum number of times the coupon can be used

    private Long currentRedemptions; // Number of times the coupon has been used

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product applicableProduct; // Coupon applicable to a specific product

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category applicableCategory;

}