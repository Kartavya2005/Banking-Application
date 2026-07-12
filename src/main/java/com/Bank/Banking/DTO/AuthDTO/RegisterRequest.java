package com.Bank.Banking.DTO.AuthDTO;

import com.Bank.Banking.Enum.Gender;
import com.Bank.Banking.Enum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Email is required")
    @Email
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "PhoneNumber is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;
}
