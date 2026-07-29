package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.DTO.CustomerDTO.UpdateCustomerRequest;
import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Enum.KYC;
import com.Bank.Banking.Mapper.CustomerMapper;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Service.NotificationService;
import com.Bank.Banking.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

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
        Customer customer = getLoggedInCustomer();
        log.info("Fetching profile for customer: {}", customer.getUser().getEmail());
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
        customer.setUpdatedAt(Instant.now());
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer profile updated successfully: {}",
                updatedCustomer.getUser().getEmail());
        notificationService.notifyUser(
                updatedCustomer.getUser().getId(),
                "Profile updated",
                "Your customer profile was updated successfully.",
                "PROFILE",
                String.valueOf(updatedCustomer.getCustomerId())
        );
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
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new RuntimeException("New password cannot be the same as the current password.");
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
        notificationService.notifyUser(
                user.getId(),
                "Password changed",
                "Your account password was changed successfully.",
                "SECURITY",
                String.valueOf(customer.getCustomerId())
        );
    }

    @Override
    public void completeKYC() {
        Customer customer = getLoggedInCustomer();
        if (customer.getKyc() == null || customer.getKyc() == KYC.PENDING) {
            customer.setKyc(KYC.APPROVED);
            customer.setUpdatedAt(Instant.now());
            customerRepository.save(customer);
            log.info("KYC completed for customer: {}", customer.getUser().getEmail());
            notificationService.notifyRole(
                    com.Bank.Banking.Enum.Role.ADMIN,
                    "Customer KYC completed",
                    "A customer has completed KYC and is ready for review.",
                    "KYC",
                    String.valueOf(customer.getCustomerId())
            );
        } else {
            throw new RuntimeException("KYC is already completed for this customer.");
        }
    }
}
