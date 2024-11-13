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
public class ProvidersDTO {

    private int statusCode;
    private String error;
    private String message;
    private String name;
    private String companyName;
    private String email;
    private String mobile_number;
    private ProviderType type;
    private boolean status;
    private ProviderDTO provider;
    private List<ProviderDTO> providerList;
    private AddressDTO address;

}
