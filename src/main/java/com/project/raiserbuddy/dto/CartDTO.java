package com.project.raiserbuddy.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartDTO {

    private Integer cartId;

    private UserDTO user;

    private Set<CartItemDTO> cartItems;

    private double totalPrice;

    private int totalItem;

    private int totalDiscountedPrice;

    private int discount;

}