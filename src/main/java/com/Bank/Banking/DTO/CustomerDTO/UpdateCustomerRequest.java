package com.Bank.Banking.DTO.CustomerDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {
    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String address;
}
