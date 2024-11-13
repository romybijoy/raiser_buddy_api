package com.project.raiserbuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.raiserbuddy.enums.ProviderType;
import com.project.raiserbuddy.enums.Role;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderDTO {

    private Integer id;
    private String email;
    private String name;
    private String companyName;
    private String mobile_number;
    private ProviderType type;
    private boolean status;
    private AddressDTO address;

}
