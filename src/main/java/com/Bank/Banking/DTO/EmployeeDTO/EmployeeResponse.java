package com.Bank.Banking.DTO.EmployeeDTO;

import com.Bank.Banking.Enum.Branch;
import com.Bank.Banking.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    private long employeeId;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
    private String department;
    private String designation;
    private Branch branch;
    private LocalDate joiningDate;
    private double salary;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isActive;
    private String address;
}
