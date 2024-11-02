package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.Reviews;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    private int statusCode;
    private String error;
    private String token;
    private String message;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String shortDesc;
    private String desc;
    private Double avgRating;

    private Double price;

    private Double quantity;
    private List<String> images;
    private ProdDTO product;
    private List<ProdDTO> productList;
    private CatgryDTO category_id;
    private List<ReviewsDTO> reviews;
    private boolean status;
}
