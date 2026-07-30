package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.BankAccountDTO.BankAccountResponse;
import com.Bank.Banking.DTO.BankAccountDTO.CreateBankAccountRequest;
import com.Bank.Banking.DTO.BankAccountDTO.UpdateBankAccountRequest;
import com.Bank.Banking.Entity.BankAccount;
import com.Bank.Banking.Mapper.BankAccountMapper;
import com.Bank.Banking.Service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    public final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<BankAccountResponse> createAccount(
            @Valid @RequestBody CreateBankAccountRequest request) {

        BankAccount bankAccount = bankAccountService.createAccount(request);
        BankAccountResponse response = BankAccountMapper.toResponse(bankAccount);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<BankAccountResponse> updateAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody UpdateBankAccountRequest request) {

        BankAccount bankAccount = bankAccountService.updateAccount(accountId, request);
        BankAccountResponse response = BankAccountMapper.toResponse(bankAccount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getAccountByNumber(
            @PathVariable String accountNumber) {

        BankAccountResponse response = bankAccountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BankAccountResponse>> getCustomerAccounts(
            @PathVariable Long customerId) {

        List<BankAccountResponse> response = bankAccountService.getCustomerAccounts(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<BankAccountResponse>> getMyAccounts() {
        // This endpoint would typically retrieve accounts for the authenticated user.
        // The service layer handles the logic for determining the current user.
        List<BankAccountResponse> response = bankAccountService.getMyAccounts();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> getAllAccounts() {
        List<BankAccountResponse> response = bankAccountService.getAllAccounts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BankAccountResponse>> searchAccounts(
            @RequestParam String keyword) {

        List<BankAccountResponse> response = bankAccountService.searchAccounts(keyword);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}/freeze")
    public ResponseEntity<String> freezeAccount(
            @PathVariable Long accountId) {

        bankAccountService.freezeAccount(accountId);
        return ResponseEntity.ok("Account with ID " + accountId + " frozen successfully.");
    }

    @PutMapping("/{accountId}/unfreeze")
    public ResponseEntity<String> unfreezeAccount(
            @PathVariable Long accountId) {

        bankAccountService.unfreezeAccount(accountId);
        return ResponseEntity.ok("Account with ID " + accountId + " unfrozen successfully.");
    }

    @PutMapping("/{accountId}/close")
    public ResponseEntity<String> closeAccount(
            @PathVariable Long accountId) {

        bankAccountService.closeAccount(accountId);
        return ResponseEntity.ok("Account with ID " + accountId + " closed successfully.");
    }
}
