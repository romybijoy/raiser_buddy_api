package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.Rating;
import com.project.raiserbuddy.entity.Reviews;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProdDTO {


    private Integer productId;

    private String name;

    private List<String> images;
    private String shortDesc;
    private String desc;
    private Double avgRating;
    private Double price;
    private Double quantity;

    private List<ReviewsDTO> reviews;

    private List<RatingDTO> ratings;

    private CatgryDTO category;

    private boolean status;

    private double discount;
    private double specialPrice;
    private LocalDateTime createdAt;
}
