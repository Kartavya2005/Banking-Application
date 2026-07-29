package com.Bank.Banking.Entity;


import com.Bank.Banking.Enum.AccountStatus;
import com.Bank.Banking.Enum.AccountType;
import com.Bank.Banking.Enum.Branch;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Branch branch;

    private String ifsc;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
