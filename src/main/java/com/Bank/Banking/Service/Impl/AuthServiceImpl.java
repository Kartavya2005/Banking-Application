package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.AuthDTO.AuthResponse;
import com.Bank.Banking.DTO.AuthDTO.LoginRequest;
import com.Bank.Banking.DTO.AuthDTO.RegisterRequest;
import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Enum.KYC;
import com.Bank.Banking.Enum.Role;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Security.JwtService;
import com.Bank.Banking.Service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        AuthUser authUser =
                userRepository.findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() -> {
                            log.error(
                                    "User not found with email: {}",
                                    request.getEmail()
                            );
                            return new RuntimeException(
                                    "User not found"
                            );
                        });
        log.info(
                "User authenticated successfully: {}",
                authUser.getEmail()
        );
        String token = jwtService.generateToken(
                new User(authUser.getEmail(), authUser.getPassword(), Collections.emptyList()), authUser.getRole());
        log.info(
                "JWT token generated for user: {}",
                authUser.getEmail()
        );
        return AuthResponse.builder()
                .token(token)
                .role(authUser.getRole())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already Exists");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone Number already exists");
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

        customerRepository.save(customer);

        String token = jwtService.generateToken(
                new User(authUser.getEmail(), authUser.getPassword(), Collections.emptyList()), authUser.getRole());

        return AuthResponse.builder()
                .token(token)
                .role(authUser.getRole())
                .build();
    }
}
