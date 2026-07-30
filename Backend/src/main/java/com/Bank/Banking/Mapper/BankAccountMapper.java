package com.Bank.Banking.Mapper;

import com.Bank.Banking.DTO.BankAccountDTO.BankAccountResponse;
import com.Bank.Banking.Entity.BankAccount;

public class BankAccountMapper {

    public static BankAccountResponse toResponse(BankAccount account) {

        if (account == null) {
            return null;
        }

        return BankAccountResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(
                        account.getCustomer().getFirstName() + " "
                                + account.getCustomer().getLastName()
                )
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .branch(account.getBranch())
                .ifscCode(account.getBranch().getIfscCode())
                .accountStatus(account.getAccountStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
}