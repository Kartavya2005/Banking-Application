package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.DTO.CustomerDTO.UpdateCustomerRequest;
import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Mapper.CustomerMapper;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Customer getLoggedInCustomer() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return customerRepository.findByUserEmail(email) // Corrected method name
                .orElseThrow(() ->
                        new RuntimeException("Customer not found for email: " + email));
    }


    @Override
    public CustomerResponse getMyProfile() {
        log.info("Fetching logged-in customer profile");
        Customer customer = getLoggedInCustomer();
        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse updateProfile(UpdateCustomerRequest request) {
        Customer customer = getLoggedInCustomer();
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            customer.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            customer.setLastName(request.getLastName().trim());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
            // Check if phone number is already used by another user
            if (!request.getPhoneNumber().equals(customer.getUser().getPhoneNumber())
                    && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new RuntimeException("Phone number already exists.");
            }
            customer.getUser().setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            // Check if email is already used by another user
            if (!request.getEmail().equals(customer.getUser().getEmail())
                    && userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists.");
            }
            customer.getUser().setEmail(request.getEmail().trim());
        }
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            customer.setAddress(request.getAddress().trim());
        }
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer profile updated successfully: {}",
                updatedCustomer.getUser().getEmail());
        return CustomerMapper.toResponse(updatedCustomer);
    }

    @Override
    public void changePassword(ChangePasswordDTO request) {
        Customer customer = getLoggedInCustomer();
        AuthUser user = customer.getUser();
        // Check current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {
            throw new RuntimeException("Current password is incorrect.");
        }
        // Check new password and confirm password
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }
        // Encode new password
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );
        userRepository.save(user);
        log.info("Password changed successfully for {}",
                user.getEmail());
    }
}
