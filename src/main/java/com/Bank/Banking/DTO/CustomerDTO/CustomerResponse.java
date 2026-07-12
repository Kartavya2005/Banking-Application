package com.Bank.Banking.DTO.CustomerDTO;

import com.Bank.Banking.Enum.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

    private Long customerId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private Role role;

    private Branch branch;

    private KYC kyc;

    private LocalDate DOB;

    private long Adhar;

    private long PAN;

    private Gender gender;
}
