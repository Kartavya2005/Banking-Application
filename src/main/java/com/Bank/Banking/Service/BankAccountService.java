package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.BankAccountDTO.BankAccountResponse;
import com.Bank.Banking.DTO.BankAccountDTO.CreateBankAccountRequest;
import com.Bank.Banking.DTO.BankAccountDTO.UpdateBankAccountRequest;
import com.Bank.Banking.Entity.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount createAccount(CreateBankAccountRequest request);

    BankAccount updateAccount(Long accountId,
                              UpdateBankAccountRequest request);

    BankAccountResponse getAccountByNumber(String accountNumber);

    List<BankAccountResponse> getCustomerAccounts(Long customerId);

    List<BankAccountResponse> getMyAccounts();

    List<BankAccountResponse> getAllAccounts();

    List<BankAccountResponse> searchAccounts(String keyword);

    void freezeAccount(Long accountId);

    void unfreezeAccount(Long accountId);

    void closeAccount(Long accountId);
}
