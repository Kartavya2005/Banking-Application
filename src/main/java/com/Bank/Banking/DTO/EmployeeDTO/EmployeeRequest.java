package com.Bank.Banking.DTO.EmployeeDTO;

import com.Bank.Banking.Entity.AuthUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String address;
}
