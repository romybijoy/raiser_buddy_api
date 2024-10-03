package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.enums.Role;
import com.project.raiserbuddy.enums.Status;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersDTO {

    private int statusCode;
    private String error;
    private String token;
    private String message;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String city;
    private String email;
    private String mobile_number;
    private String password;
    private Role role;
    private String image;
    private String block_reason;
    private boolean enabled;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;
    private List<Address> addresses;

}
