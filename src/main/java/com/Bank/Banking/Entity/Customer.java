package com.Bank.Banking.Entity;

import com.Bank.Banking.Enum.AccountStatus;
import com.Bank.Banking.Enum.AccountType;
import com.Bank.Banking.Enum.Branch;
import com.Bank.Banking.Enum.KYC;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AuthUser user;

    private String firstName;

    private String lastName;

    private String Address;

    private LocalDate DOB;

    private long Adhar;

    private long PAN;

    private Instant createdAt;

    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    private KYC kyc;


}
