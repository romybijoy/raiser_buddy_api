package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemsDTO {

    private ProdDTO product;

    private String size;

    private int quantity;

    private double price;

    private double discountedPrice;

    private Integer userId;

    private LocalDateTime deliveryDate;
}
