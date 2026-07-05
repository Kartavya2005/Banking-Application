package com.Bank.Banking.DTO;

import com.Bank.Banking.Enum.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}
