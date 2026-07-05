package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.AuthResponse;
import com.Bank.Banking.DTO.LoginRequest;
import com.Bank.Banking.DTO.RegisterRequest;
import com.Bank.Banking.Entity.AppUser;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Security.AppUserPrincipal;
import com.Bank.Banking.Security.JwtService;
import com.Bank.Banking.Service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        AppUser appUser =
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
                appUser.getEmail()
        );
        String token = jwtService.generateToken(
                new AppUserPrincipal(appUser),appUser.getRole());
        log.info(
                "JWT token generated for user: {}",
                appUser.getEmail()
        );
        return AuthResponse.builder()
                .token(token)
                .role(appUser.getRole())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already Exists");
        }
        AppUser appUser = AppUser.builder()
                .email(request.getEmail())
                .Password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(appUser);
        String token = jwtService.generateToken(
                new AppUserPrincipal(appUser),appUser.getRole());

        return AuthResponse.builder()
                .token(token)
                .role(appUser.getRole())
                .build();
    }
}
