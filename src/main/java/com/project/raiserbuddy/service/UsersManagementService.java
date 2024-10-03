package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.*;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.enums.Role;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.UserException;
import com.project.raiserbuddy.repository.UsersRepository;
import com.project.raiserbuddy.util.EmailUtil;
import com.project.raiserbuddy.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersManagementService {

    private static final Logger log = LogManager.getLogger(UsersManagementService.class);
    @Autowired
    private UsersRepository usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    @Lazy
    private CartService cartService;

    public OurUsers findUserById(Integer userId) throws UserException {
        Optional<OurUsers> user=usersRepo.findById(userId);

        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("user not found with id "+userId);
    }

//    public OurUsers findUserProfileByJwt(String jwt) throws UserException {
//        System.out.println("user service");
//        String email=jwtTokenProvider.getEmailFromJwtToken(jwt);
//
//        System.out.println("email"+email);
//
//        User user=userRepository.findByEmail(email);
//
//        if(user==null) {
//            throw new UserException("user not exist with email "+email);
//        }
//        System.out.println("email user"+user.getEmail());
//        return user;
//    }

    public UsersDTO register(UsersDTO registrationRequest){
        UsersDTO resp = new UsersDTO();
        Optional<OurUsers> users = usersRepo.findByEmail(registrationRequest.getEmail());

        if (users.isPresent()) {
            throw new APIException("User with the email '" + registrationRequest.getEmail() + "' already exists !!!", 409);
        }
        String otp = otpUtil.generateOtp();
        if(!Objects.equals(registrationRequest.getEmail(), "")) {

            try {
                emailUtil.sendOtpEmail(registrationRequest.getEmail(), otp);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp please try again");
            }
        }
        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setAddresses(registrationRequest.getAddresses());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            ourUser.setImage(registrationRequest.getImage());
            ourUser.setMobile_number(registrationRequest.getMobile_number());
            ourUser.setOtp(otp);
            ourUser.setOtpGeneratedTime(LocalDateTime.now());
            ourUser.setEnabled(true);
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers ourUsersResult = usersRepo.save(ourUser);

            if (ourUsersResult.getId()>0) {
                cartService.createCart(ourUsersResult);
                resp.setOurUsers((ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public UsersDTO login(UsersDTO loginRequest){
        UsersDTO response = new UsersDTO();
        var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
        response.setBlock_reason(user.getBlock_reason());
        try {

            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        }catch (DisabledException e) {

            response.setMessage("User is disabled due to "+ user.getBlock_reason());
            response.setStatusCode(403);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }





    public UsersDTO refreshToken(UsersDTO refreshTokenRequest){
        UsersDTO response = new UsersDTO();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

//
//    public UsersDTO getAllUsers(String keyword, String role) {
//        UsersDTO usersDTO = new UsersDTO();
//        List<OurUsers> result;
//        try {
//            if(!keyword.isEmpty() || !role.isEmpty()){
//               result = usersRepo.search(keyword, role);
//            }else{
//            result = usersRepo.findAllByStatus();
//            }
//            if (!result.isEmpty()) {
//                usersDTO.setOurUsersList(result);
//                usersDTO.setStatusCode(200);
//                usersDTO.setMessage("Successful");
//            } else {
//                usersDTO.setStatusCode(404);
//                usersDTO.setMessage("No users found");
//            }
//            return usersDTO;
//        } catch (Exception e) {
//            usersDTO.setStatusCode(500);
//            usersDTO.setMessage("Error occurred: " + e.getMessage());
//            return usersDTO;
//        }
//    }

    public UsersResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Boolean enabled) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<OurUsers> pageUsers;
        pageUsers = usersRepo.findByEnabled(enabled, pageDetails);

        List<OurUsers> users = pageUsers.getContent();

        if (users.isEmpty()) {
            throw new APIException("No users is created till now", 404);
        }


      UsersResponse usersResponse = new UsersResponse();

        usersResponse.setMessage("Users fetched Successfully");
        usersResponse.setStatusCode(302);
        usersResponse.setContent(users);
        usersResponse.setPageNumber(pageUsers.getNumber());
        usersResponse.setPageSize(pageUsers.getSize());
        usersResponse.setTotalElements(pageUsers.getTotalElements());
        usersResponse.setTotalPages(pageUsers.getTotalPages());
        usersResponse.setLastPage(pageUsers.isLast());

        return usersResponse;
    }


    public UsersResponse searchUsersByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<OurUsers> pageUsers = usersRepo.findByNameOrEmailIgnoreCaseContainingAndEnabled(keyword, keyword, true, pageDetails);

        List<OurUsers> users = pageUsers.getContent();

        if (users.size() == 0) {
            throw new APIException("Users not found with keyword: " + keyword, 404);
        }

//        List<UsersDTO> usersDTOs = users.stream().map(p -> modelMapper.map(p, UsersDTO.class))
//                .collect(Collectors.toList());

        UsersResponse usersResponse = new UsersResponse();

        usersResponse.setMessage("Users fetched Successfully");
        usersResponse.setStatusCode(302);
        usersResponse.setContent(users);
        usersResponse.setPageNumber(pageUsers.getNumber());
        usersResponse.setPageSize(pageUsers.getSize());
        usersResponse.setTotalElements(pageUsers.getTotalElements());
        usersResponse.setTotalPages(pageUsers.getTotalPages());
        usersResponse.setLastPage(pageUsers.isLast());

        return usersResponse;
    }

      public UsersDTO getUsersById(Integer id) {
        UsersDTO usersDTO = new UsersDTO();
        try {
            OurUsers usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            usersDTO.setOurUsers(usersById);
            usersDTO.setStatusCode(200);
            usersDTO.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            usersDTO.setStatusCode(500);
            usersDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return usersDTO;
    }

    public UsersDTO getUsersByRole(Role role) {
        UsersDTO usersDTO = new UsersDTO();
        try {
            List<OurUsers> users = usersRepo.findByRoleContaining(String.valueOf(role));
            usersDTO.setOurUsersList(users);
            usersDTO.setStatusCode(200);
            usersDTO.setMessage("Users with role '" + role + "' found successfully");
        } catch (Exception e) {
            usersDTO.setStatusCode(500);
            usersDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return usersDTO;
    }

    public UsersDTO deleteUser(Integer userId) {
        UsersDTO usersDTO = new UsersDTO();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                usersDTO.setStatusCode(200);
                usersDTO.setMessage("User deleted successfully");
            } else {
                usersDTO.setStatusCode(404);
                usersDTO.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            usersDTO.setStatusCode(500);
            usersDTO.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return usersDTO;
    }

    public UsersDTO updateUser(Integer userId, OurUsers updatedUser) {
        UsersDTO usersDTO = new UsersDTO();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setAddresses(updatedUser.getAddresses());
                existingUser.setMobile_number(updatedUser.getMobile_number());
                existingUser.setRole(updatedUser.getRole());
                existingUser.setImage(updatedUser.getImage());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                usersDTO.setOurUsers(savedUser);
                usersDTO.setStatusCode(200);
                usersDTO.setMessage("User updated successfully");
            } else {
                usersDTO.setStatusCode(404);
                usersDTO.setMessage("User not found for update");
            }
        } catch (Exception e) {
            usersDTO.setStatusCode(500);
            usersDTO.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return usersDTO;
    }


    public UsersDTO getMyInfo(String email){
        UsersDTO usersDTO = new UsersDTO();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                usersDTO.setOurUsers(userOptional.get());
                usersDTO.setStatusCode(200);
                usersDTO.setMessage("successful");
            } else {
                usersDTO.setStatusCode(404);
                usersDTO.setMessage("User not found for update");
            }

        }catch (Exception e){
            usersDTO.setStatusCode(500);
            usersDTO.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return usersDTO;

    }

    public UsersDTO blockUser(Integer userId, UsersDTO req) {
        UsersDTO usersDTO = new UsersDTO();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEnabled(false);
                existingUser.setBlock_reason(req.getBlock_reason());
                usersRepo.updateBlockInfo(req.getBlock_reason(), userId);
                usersDTO.setStatusCode(200);
                usersDTO.setMessage("User blocked successfully");
            } else {
                usersDTO.setStatusCode(404);
                usersDTO.setMessage("User not found for blocking");
            }
        } catch (Exception e) {
            usersDTO.setStatusCode(500);
            usersDTO.setMessage("Error occurred while blocking user: " + e.getMessage());
        }
        return usersDTO;
    }


    public UsersDTO verifyAccount(String email, String otp) {
        UsersDTO usersDTO = new UsersDTO();
        OurUsers user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60 * 60)) {
            user.setEnabled(true);
            usersRepo.save(user);
            usersDTO.setStatusCode(200);
            usersDTO.setMessage("OTP verified you can login");
            return usersDTO;
        }
        usersDTO.setStatusCode(500);
        usersDTO.setMessage("Please regenerate otp and try again");
        return usersDTO;
    }

    public UsersDTO regenerateOtp(String email) {
        UsersDTO usersDTO = new UsersDTO();
        OurUsers user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        usersRepo.save(user);
        usersDTO.setStatusCode(200);
        usersDTO.setMessage("Email sent... please verify account within 1 minute");
        return usersDTO;
    }

    public UsersDTO forgotPassword(String email) {
        UsersDTO usersDTO = new UsersDTO();
        OurUsers user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        try {
            emailUtil.sendSetPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send set password email please try again");
        }

        usersDTO.setStatusCode(200);
        usersDTO.setMessage("Please check your email to set new password to your account");
        return usersDTO;
    }

    public UsersDTO setPassword( String email, String newPassword){
        UsersDTO resp = new UsersDTO();
        try {
       OurUsers user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));

            user.setPassword(passwordEncoder.encode(newPassword));
         usersRepo.save(user);

            resp.setMessage("Password reset Successfully");
            resp.setStatusCode(200);
        } catch (NoSuchElementException e){
            resp.setStatusCode(404);
            resp.setError(e.getMessage());
        } catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
        }
}
