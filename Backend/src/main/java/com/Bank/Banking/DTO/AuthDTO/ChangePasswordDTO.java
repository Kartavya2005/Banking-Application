package com.Bank.Banking.DTO.AuthDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
