package com.Bank.Banking.DTO.BankAccountDTO;

import com.Bank.Banking.Enum.AccountStatus;
import com.Bank.Banking.Enum.AccountType;
import com.Bank.Banking.Enum.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponse {

    private Long accountId;

    private String accountNumber;

    private String accountHolderName;

    private AccountType accountType;

    private BigDecimal balance;

    private Branch branch;

    private String ifscCode;

    private AccountStatus accountStatus;

    private Instant createdAt;
}