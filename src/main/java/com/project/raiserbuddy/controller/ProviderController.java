package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.ProviderRequest;
import com.project.raiserbuddy.dto.ProvidersDTO;
import com.project.raiserbuddy.dto.ProviderResponse;
import com.project.raiserbuddy.entity.Provider;
import com.project.raiserbuddy.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
public class ProviderController {

@Autowired
private ProviderService providerService;

    @PostMapping()
    public ResponseEntity<ProvidersDTO> register(@Valid @RequestBody ProviderRequest req){
        ProvidersDTO response = providerService.create(req);

        if(response.getStatusCode() == 500){
            return new ResponseEntity<ProvidersDTO>(response, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping()
    public ResponseEntity<ProviderResponse> getAllProviders(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                     @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                     @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
                                                     @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
                                                     @RequestParam(name = "enabled", defaultValue = AppConstants.STATUS, required = false) boolean enabled){

        ProviderResponse usersResponse = providerService.getAllProviders(pageNumber, pageSize, sortBy, sortOrder,enabled);

        return new ResponseEntity<ProviderResponse>(usersResponse, HttpStatus.FOUND);
    }


    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<ProviderResponse> getProvidersByKeyword(@PathVariable String keyword,
                                                           @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                           @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                           @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
                                                           @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProviderResponse usersResponse = providerService.searchProvidersByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProviderResponse>(usersResponse, HttpStatus.FOUND);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProvidersDTO> getProviderByID(@PathVariable Integer id){
        ProvidersDTO response = providerService.getProvidersById(id);
        System.out.println(response);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<ProvidersDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProvidersDTO> updateProvider(@PathVariable Integer id, @RequestBody Provider reqres){
        ProvidersDTO response = providerService.updateProvider(id, reqres);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<ProvidersDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProvidersDTO> deleteUSer(@PathVariable Integer id){
        return ResponseEntity.ok(providerService.deleteProvider(id));
    }

    @PutMapping("/block/{userId}")
    public ResponseEntity<ProvidersDTO> blockProvider(@PathVariable Integer id, @RequestBody ProvidersDTO req){
        ProvidersDTO response = providerService.blockProvider(id, req);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<ProvidersDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }
}
