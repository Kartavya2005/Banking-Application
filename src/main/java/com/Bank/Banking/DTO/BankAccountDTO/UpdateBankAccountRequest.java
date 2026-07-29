package com.Bank.Banking.DTO.BankAccountDTO;

import com.Bank.Banking.Enum.AccountStatus;
import com.Bank.Banking.Enum.Branch;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBankAccountRequest {

    @Enumerated(EnumType.STRING)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}