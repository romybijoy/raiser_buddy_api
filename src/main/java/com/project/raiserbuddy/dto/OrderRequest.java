package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderRequest
 *
 * @author Romyb
 * @since 21/04/2026
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private Address address;
    private String couponCode;
    private boolean useWallet;

}
