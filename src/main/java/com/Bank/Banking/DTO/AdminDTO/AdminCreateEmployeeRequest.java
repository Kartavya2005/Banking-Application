package com.Bank.Banking.DTO.AdminDTO;

import com.Bank.Banking.Enum.Branch;
import com.Bank.Banking.Enum.Role;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateEmployeeRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String PhoneNumber;
    private Role role;
    private String department;
    private String Password;
    private String designation;
    private Branch branch;
    private LocalDate joiningDate;
    private double salary;
    private boolean isActive;
    private String address;}
