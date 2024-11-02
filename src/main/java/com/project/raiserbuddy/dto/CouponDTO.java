package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponDTO {

    private int statusCode;
    private String error;
    private String token;
    private String message;
    private String code;

    private double discount;

    private String discountType;

    private Date validFrom;

    private Date validUntil;

    private boolean status;

    private Integer userId;

    private Long maxRedemptions;

    private Long currentRedemptions;


    private Product applicableProduct;

    private Category applicableCategory;
}
