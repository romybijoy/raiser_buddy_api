package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.OurUsers;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO {

    private int statusCode;
    private String error;
    private String token;
    private String message;
    private String name;
    private String desc;
    private String image;
    private Boolean status;
    private Category category;
    private List<Category> categories;
}
