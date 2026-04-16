package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.AddressDTO;
import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.repository.AddressRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * AddressService
 *
 * @author Romyb
 * @since 16/04/2026
 */


@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UsersRepository usersRepository;

    // 🔥 Get current logged user
    private OurUsers getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //   CREATE ADDRESS
    public Address addAddress(AddressDTO dto) {

        OurUsers user = getCurrentUser();

        Address address = new Address();
        address.setHouse_name(dto.getHouse_name());
        address.setPlace(dto.getPlace());
        address.setDistrict(dto.getDistrict());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setZipcode(dto.getZipcode());
        address.setMobile(dto.getMobile());
        address.set_default(dto.is_default());

        address.setUser(user);

        return addressRepository.save(address);
    }

    //   GET USER ADDRESSES
    public List<Address> getUserAddresses() {
        OurUsers user = getCurrentUser();
        return addressRepository.findByUserId(user.getId());
    }

    //   UPDATE ADDRESS
    public Address updateAddress(Integer id, AddressDTO dto) {

        OurUsers user = getCurrentUser();

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 🔒 SECURITY CHECK
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        address.setHouse_name(dto.getHouse_name());
        address.setPlace(dto.getPlace());
        address.setDistrict(dto.getDistrict());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setZipcode(dto.getZipcode());
        address.setMobile(dto.getMobile());
        address.set_default(dto.is_default());

        return addressRepository.save(address);
    }

    //   DELETE ADDRESS
    public void deleteAddress(Integer id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getOrders().isEmpty()) {
            throw new RuntimeException("Cannot delete address used in orders");
        }

        addressRepository.delete(address);
    }
}
