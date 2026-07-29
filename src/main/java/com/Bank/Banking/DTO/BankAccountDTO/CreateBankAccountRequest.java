package com.Bank.Banking.DTO.BankAccountDTO;

import com.Bank.Banking.Enum.AccountType;
import com.Bank.Banking.Enum.Branch;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBankAccountRequest {
    @NotNull(message = "Customer Id is required")
    private Long customerId;

    @NotNull(message = "Account Type is required")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @NotNull(message = "Branch is required")
    @Enumerated(EnumType.STRING)
    private Branch branch;

    @NotNull(message = "Initial Deposit is required")
    @DecimalMin(value = "0.00", message = "Initial Deposit cannot be negative")
    private BigDecimal initialDeposit;

}
