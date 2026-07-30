package com.Bank.Banking.Service;

//import com.Bank.Banking.DTO.AuthDTO.AuthResponse;
//import com.Bank.Banking.DTO.AuthDTO.LoginRequest;
//import org.springframework.stereotype.Service;


import com.Bank.Banking.DTO.AuthDTO.AuthResponse;
import com.Bank.Banking.DTO.AuthDTO.LoginRequest;
import com.Bank.Banking.DTO.AuthDTO.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
