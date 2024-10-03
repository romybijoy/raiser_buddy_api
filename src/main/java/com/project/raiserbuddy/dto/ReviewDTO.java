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
public class ReviewDTO {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String text;
    private Double rating;
    private Reviews review;
    private List<Product> product;
    private OurUsers user;

}
