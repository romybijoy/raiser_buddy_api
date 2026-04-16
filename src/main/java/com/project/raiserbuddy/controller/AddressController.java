package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.dto.AddressDTO;
import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AddressController
 *
 * @author Romyb
 * @since 16/04/2026
 */


@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    //   ADD ADDRESS
    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody AddressDTO dto) {
        return ResponseEntity.ok(addressService.addAddress(dto));
    }

    //   GET ALL ADDRESSES
    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses() {
        return ResponseEntity.ok(addressService.getUserAddresses());
    }

    //   UPDATE ADDRESS
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Integer id,
            @RequestBody AddressDTO dto) {

        return ResponseEntity.ok(addressService.updateAddress(id, dto));
    }

    //   DELETE ADDRESS
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
