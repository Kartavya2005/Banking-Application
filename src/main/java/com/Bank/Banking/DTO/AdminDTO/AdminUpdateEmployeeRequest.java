package com.Bank.Banking.DTO.AdminDTO;

import com.Bank.Banking.Enum.Branch;
import com.Bank.Banking.Enum.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEmployeeRequest {

    private String firstName;
    private String lastName;
    private Role role;
    private double salary;
    private Branch branch;
    private String PhoneNumber;
    private String department;
    private String designation;
    private String address;
    private boolean isActive;
}
