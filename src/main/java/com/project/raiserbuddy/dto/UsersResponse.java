package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.OurUsers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponse {

    private int statusCode;
    private String message;
    private List<OurUsers> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;

}