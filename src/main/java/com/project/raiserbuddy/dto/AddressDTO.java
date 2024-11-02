package com.project.raiserbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Integer add_id;

    private String house_name;
    private String place;
    private String district;
    private String state;
    private String country;
    private String zipcode;
    private String mobile;
    private boolean is_default;


}
