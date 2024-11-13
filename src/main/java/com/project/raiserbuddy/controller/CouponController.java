package com.project.raiserbuddy.controller;


import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.CouponDTO;
import com.project.raiserbuddy.dto.CouponResponse;
import com.project.raiserbuddy.entity.Coupon;
import com.project.raiserbuddy.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping()
    public ResponseEntity<CouponResponse> getAllCoupons(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                           @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                           @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_COUPONS_BY, required = false) String sortBy,
                                                           @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
                                                           @RequestParam(name = "status", defaultValue = AppConstants.STATUS, required = false) boolean status){

        CouponResponse couponResponse = couponService.getCoupons(pageNumber, pageSize, sortBy, sortOrder,status);

        return new ResponseEntity<CouponResponse>(couponResponse, HttpStatus.FOUND);
    }

    @PostMapping()
    public ResponseEntity<CouponDTO> create(@Valid @RequestBody Coupon coupon){
        CouponDTO response = couponService.createCoupon(coupon);

        return new ResponseEntity<CouponDTO>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public Coupon validateCoupon(@PathVariable String code) {
        Coupon coupon = couponService.validateCoupon(code);
        if (coupon != null) {
            return coupon;
        }
        // Handle the case where the coupon is invalid or expired
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired coupon");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<CouponDTO> deleteCoupon(@PathVariable Integer id){
        return ResponseEntity.ok(couponService.deleteCoupon(id));
    }

}
