package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRequest {


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