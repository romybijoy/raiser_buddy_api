package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {

    private int statusCode;
    private String message;
    private List<Coupon> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;

}