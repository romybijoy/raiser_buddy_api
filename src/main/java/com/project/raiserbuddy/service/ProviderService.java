package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.*;
import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.Provider;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.repository.AddressRepository;
import com.project.raiserbuddy.repository.ProviderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProviderService {

    private static final Logger log = LogManager.getLogger(ProviderService.class);
    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    public ModelMapper modelMapper;

    public Provider findProviderById(Integer providerId) throws UserException {
        Optional<Provider> provider=providerRepository.findById(providerId);

        if(provider.isPresent()){
            return provider.get();
        }
        throw new UserException("Provider not found with id "+providerId);
    }

    public ProvidersDTO create(ProviderRequest request){
        ProvidersDTO resp = new ProvidersDTO();
        Optional<Provider> providers = providerRepository.findByEmail(request.getEmail());

        if (providers.isPresent()) {
            throw new APIException("Provider with the email '" + request.getEmail() + "' already exists !!!", 409);
        }

       try {
            Provider provider = new Provider();
            provider.setEmail(request.getEmail());


//           Address address = request.getAddress();
//           if (address.getAdd_id() == null) {
//               address = addressRepository.save(address);
//           }

//            provider.setAddress(address);
            provider.setType(request.getType());
            provider.setName(request.getName());
            provider.setMobile_number(request.getMobile_number());
            provider.setStatus(true);
            Provider providersResult = providerRepository.save(provider);

            if (providersResult.getId()>0) {
                ProviderDTO providerOut = modelMapper.map(providersResult, ProviderDTO.class);
                resp.setProvider(providerOut);
                resp.setMessage("Provider Saved Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }



    public ProviderResponse getAllProviders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Boolean status) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Provider> pageProviders;
        pageProviders = providerRepository.findByStatus(status, pageDetails);

        List<Provider> providers = pageProviders.getContent();


        List<ProviderDTO> providerDTOs = providers.stream().map(provider -> modelMapper.map(provider, ProviderDTO.class))
                .toList();


        if (providers.isEmpty()) {
            throw new APIException("No providers is created till now", 404);
        }


      ProviderResponse providersResponse = new ProviderResponse();

        providersResponse.setMessage("Providers fetched Successfully");
        providersResponse.setStatusCode(302);
        providersResponse.setContent(providerDTOs);
        providersResponse.setPageNumber(pageProviders.getNumber());
        providersResponse.setPageSize(pageProviders.getSize());
        providersResponse.setTotalElements(pageProviders.getTotalElements());
        providersResponse.setTotalPages(pageProviders.getTotalPages());
        providersResponse.setLastPage(pageProviders.isLast());

        return providersResponse;
    }


    public ProviderResponse searchProvidersByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Provider> pageProviders = providerRepository.findByNameOrEmailIgnoreCaseContainingAndStatus(keyword, keyword, true, pageDetails);

        List<Provider> providers = pageProviders.getContent();

        if (providers.size() == 0) {
            throw new APIException("Providers not found with keyword: " + keyword, 404);
        }

        List<ProviderDTO> providersDTOs = providers.stream().map(provider -> modelMapper.map(provider, ProviderDTO.class))
                .toList();

        ProviderResponse providersResponse = new ProviderResponse();

        providersResponse.setMessage("Providers fetched Successfully");
        providersResponse.setStatusCode(302);
        providersResponse.setContent(providersDTOs);
        providersResponse.setPageNumber(pageProviders.getNumber());
        providersResponse.setPageSize(pageProviders.getSize());
        providersResponse.setTotalElements(pageProviders.getTotalElements());
        providersResponse.setTotalPages(pageProviders.getTotalPages());
        providersResponse.setLastPage(pageProviders.isLast());

        return providersResponse;
    }

      public ProvidersDTO getProvidersById(Integer id) {
        ProvidersDTO providersDTO = new ProvidersDTO();
        try {
            Provider providersById = providerRepository.findById(id).orElseThrow(() -> new RuntimeException("Provider Not found"));

            ProviderDTO provider = modelMapper.map(providersById, ProviderDTO.class);
            providersDTO.setProvider(provider);
            providersDTO.setStatusCode(200);
            providersDTO.setMessage("Providers with id '" + id + "' found successfully");
        } catch (Exception e) {
            providersDTO.setStatusCode(500);
            providersDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return providersDTO;
    }

//    public ProvidersDTO getUsersByRole(Role role) {
//        ProvidersDTO providersDTO = new ProvidersDTO();
//        try {
//            List<Provider> providers = providerRepository.findByRoleContaining(String.valueOf(role));
//            List<ProviderDTO> providerDTO = providers.stream().map(provider -> modelMapper.map(provider, ProviderDTO.class))
//                    .toList();
//            providersDTO.setProviderList(providerDTO);
//            providersDTO.setStatusCode(200);
//            providersDTO.setMessage("Users with role '" + role + "' found successfully");
//        } catch (Exception e) {
//            providersDTO.setStatusCode(500);
//            providersDTO.setMessage("Error occurred: " + e.getMessage());
//        }
//        return providersDTO;
//    }

    public ProvidersDTO deleteProvider(Integer providerId) {
        ProvidersDTO providersDTO = new ProvidersDTO();
        try {
            Optional<Provider> providerOptional = providerRepository.findById(providerId);
            if (providerOptional.isPresent()) {
                providerRepository.deleteById(providerId);
                providersDTO.setStatusCode(200);
                providersDTO.setMessage("Provider deleted successfully");
            } else {
                providersDTO.setStatusCode(404);
                providersDTO.setMessage("Provider not found for deletion");
            }
        } catch (Exception e) {
            providersDTO.setStatusCode(500);
            providersDTO.setMessage("Error occurred while deleting provider: " + e.getMessage());
        }
        return providersDTO;
    }

    public ProvidersDTO updateProvider(Integer providerId, Provider updatedProvider) {
        ProvidersDTO providersDTO = new ProvidersDTO();
        try {
            Optional<Provider> providerOptional = providerRepository.findById(providerId);
            if (providerOptional.isPresent()) {
                Provider existingProvider = providerOptional.get();
                existingProvider.setEmail(updatedProvider.getEmail());
                existingProvider.setName(updatedProvider.getName());

                existingProvider.setAddress(updatedProvider.getAddress());
                existingProvider.setMobile_number(updatedProvider.getMobile_number());
                existingProvider.setType(updatedProvider.getType());


                Provider savedProvider = providerRepository.save(existingProvider);
                ProviderDTO provider = modelMapper.map(savedProvider, ProviderDTO.class);
                providersDTO.setProvider(provider);
                providersDTO.setStatusCode(200);
                providersDTO.setMessage("Provider updated successfully");
            } else {
                providersDTO.setStatusCode(404);
                providersDTO.setMessage("Provider not found for update");
            }
        } catch (Exception e) {
            providersDTO.setStatusCode(500);
            providersDTO.setMessage("Error occurred while updating provider: " + e.getMessage());
        }
        return providersDTO;
    }



    public ProvidersDTO blockProvider(Integer providerId, ProvidersDTO req) {
        ProvidersDTO providersDTO = new ProvidersDTO();
        try {
            Optional<Provider> providerOptional = providerRepository.findById(providerId);
            if (providerOptional.isPresent()) {
                Provider existingProvider = providerOptional.get();
                existingProvider.setStatus(false);
                providerRepository.updateBlockInfo(providerId);
                providersDTO.setStatusCode(200);
                providersDTO.setMessage("Provider blocked successfully");
            } else {
                providersDTO.setStatusCode(404);
                providersDTO.setMessage("Provider not found for blocking");
            }
        } catch (Exception e) {
            providersDTO.setStatusCode(500);
            providersDTO.setMessage("Error occurred while blocking Provider: " + e.getMessage());
        }
        return providersDTO;
    }



}
