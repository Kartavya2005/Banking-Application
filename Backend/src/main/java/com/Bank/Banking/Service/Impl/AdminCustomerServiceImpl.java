package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.AuthDTO.RegisterRequest;
import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.DTO.CustomerDTO.UpdateCustomerRequest;
import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Enum.KYC;
import com.Bank.Banking.Enum.Role;
import com.Bank.Banking.Mapper.CustomerMapper;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Service.NotificationService;
import com.Bank.Banking.Service.AdminCustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCustomerServiceImpl implements AdminCustomerService {

    public final UserRepository userRepository;
    public final CustomerRepository customerRepository;
    public final PasswordEncoder passwordEncoder;
    public final NotificationService notificationService;

    @Override
    @Transactional
    public CustomerResponse createCustomer(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists.");
        }

        AuthUser authUser = AuthUser.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();

        userRepository.save(authUser);

        Customer customer = new Customer();

        customer.setUser(authUser);
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setAddress(request.getAddress());
        customer.setDOB(request.getDob());

        customer.setCreatedAt(Instant.now());
        customer.setUpdatedAt(Instant.now());

        customer.setKyc(KYC.PENDING);

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully by Admin: {}",
                savedCustomer.getUser().getEmail());

        notificationService.notifyUser(
                savedCustomer.getUser().getId(),
                "Customer profile created",
                "Your customer profile has been created successfully. You can now complete your profile and proceed with KYC.",
                "CUSTOMER",
                String.valueOf(savedCustomer.getCustomerId())
        );

        return CustomerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

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

        log.info("Customer profile updated successfully by Admin: {}",
                updatedCustomer.getUser().getEmail());

        return CustomerMapper.toResponse(updatedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        return CustomerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerResponse> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUser_EmailContainingIgnoreCase(
                keyword, keyword, keyword);
        return customers.stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public void resetCustomerPassword(Long customerId, ChangePasswordDTO request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        AuthUser user = customer.getUser();

        if (request.getNewPassword().equals(request.getCurrentPassword())) {
            throw new RuntimeException("New password cannot be the same as the current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Customer password reset successfully by Admin: {}",
                user.getEmail());

        notificationService.notifyUser(
                user.getId(),
                "Password reset",
                "Your password has been reset by an admin/employee. Please use the new password and change it after login.",
                "SECURITY",
                String.valueOf(customerId)
        );
    }

    @Override
    public void approveKyc(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        customer.setKyc(KYC.APPROVED);
        customer.setUpdatedAt(Instant.now());
        customerRepository.save(customer);

        log.info("KYC approved for customer by Admin: {}",
                customer.getUser().getEmail());

        notificationService.notifyUser(
                customer.getUser().getId(),
                "KYC approved",
                "Your KYC has been approved. You can now continue with banking operations that require verification.",
                "KYC",
                String.valueOf(customerId)
        );
    }

    @Override
    public void rejectKyc(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        customer.setKyc(KYC.REJECTED);
        customer.setUpdatedAt(Instant.now());
        customerRepository.save(customer);

        log.info("KYC rejected for customer by Admin: {}",
                customer.getUser().getEmail());

        notificationService.notifyUser(
                customer.getUser().getId(),
                "KYC rejected",
                "Your KYC has been rejected. Please review the required details and resubmit or contact support.",
                "KYC",
                String.valueOf(customerId)
        );
    }
}
