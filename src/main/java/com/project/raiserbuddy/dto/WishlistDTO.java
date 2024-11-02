package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.Wishlist;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WishlistDTO {
    private int statusCode;
    private String error;
    private String token;
    private String message;
    private Integer id;

    private UserDTO user;

    private ProdDTO product;

    private WishDTO wishlist;
    private List<WishDTO> wishlists;
}
