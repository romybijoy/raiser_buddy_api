package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.CouponDTO;
import com.project.raiserbuddy.dto.CouponResponse;
import com.project.raiserbuddy.entity.Coupon;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.ResourceNotFoundException;
import com.project.raiserbuddy.repository.CouponRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CouponResponse getCoupons(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Boolean status) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Coupon> pageCategories;
        pageCategories = couponRepository.findByStatus(status, pageDetails);

        List<Coupon> coupons = pageCategories.getContent();

        if (coupons.isEmpty()) {
            throw new APIException("No Coupon is created till now", 404);
        }

//        List<Category> categoryDTOs = categories.stream().collect(Collectors.toList());

        CouponResponse couponResponse = new CouponResponse();

        couponResponse.setMessage("Category fetched Successfully");
        couponResponse.setStatusCode(302);
        couponResponse.setContent(coupons);
        couponResponse.setPageNumber(pageCategories.getNumber());
        couponResponse.setPageSize(pageCategories.getSize());
        couponResponse.setTotalElements(pageCategories.getTotalElements());
        couponResponse.setTotalPages(pageCategories.getTotalPages());
        couponResponse.setLastPage(pageCategories.isLast());

        return couponResponse;
    }

    public CouponDTO  createCoupon(Coupon coupon){
        CouponDTO resp = new CouponDTO();
        Coupon savedCoupon = couponRepository.findByCode(coupon.getCode());

        if (savedCoupon != null) {
            throw new APIException("Coupon with the code '" + coupon.getCode() + "' already exists !!!", 409);
        }
        savedCoupon = couponRepository.save(coupon);

        System.out.println(savedCoupon);
        resp.setMessage("Coupon Saved Successfully");
        resp.setStatusCode(201);
        modelMapper.map(savedCoupon, resp);

        return resp;
    }

    public Coupon validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon != null && coupon.getValidUntil().after(new Date())) {
            return coupon;
        }
        return null;
    }

    public CouponDTO deleteCoupon(Integer id) {
        CouponDTO couponDTO = new CouponDTO();
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", id, 404));

        couponRepository.updateStatus(id);
        couponDTO.setStatusCode(200);
        couponDTO.setMessage("Coupon deleted successfully");

        return couponDTO;
    }

}