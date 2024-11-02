package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.enums.Role;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Integer id;
    private String name;
    private String city;
    private String email;
    private String mobile_number;
    private Role role;
    private String image;
    private String block_reason;
    private Double walletBalance;
    private boolean enabled;
    private List<AddressDTO> addresses;

}
