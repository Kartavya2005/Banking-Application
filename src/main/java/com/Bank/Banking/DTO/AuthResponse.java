package com.Bank.Banking.DTO;

import com.Bank.Banking.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
//    private String token;
//    private Role role;
    private String token;
    private Role role;
}
