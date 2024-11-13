package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Provider;
import com.project.raiserbuddy.enums.ProviderType;
import com.project.raiserbuddy.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRequest {


    private String name;
    private String email;
    private String mobile_number;
    private String companyName;
    private ProviderType type;
    private boolean status;
    private Provider provider;

    private List<Provider> providerList;
    private Address address;
}