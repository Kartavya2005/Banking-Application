package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.AuthDTO.AuthResponse;
import com.Bank.Banking.DTO.AuthDTO.LoginRequest;
import com.Bank.Banking.DTO.AuthDTO.RegisterRequest;
import com.Bank.Banking.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) throws Exception {
        // Validate request
        log.info("Login request received for email: {}", request.getEmail());
        if (request.getEmail() == null || request.getPassword() == null) {
            log.warn("Login request failed for email: {}. Email and password are required.", request.getEmail());
            throw new Exception("Email and password are required");
        }
        AuthResponse response = authService.login(request);
        log.info("Login successful for email: {}", request.getEmail());
        return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) throws Exception {
        log.info("Register request received for email: {}", request.getEmail());
        if(request.getEmail() == null || request.getPassword() == null){
            log.info("Register request failed for email: {}. Email, Password and Role are required.");
            throw new Exception("Email, Password and Role are required");
        }
        AuthResponse response = authService.register(request);
        log.info("Register Succesfull for email: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
