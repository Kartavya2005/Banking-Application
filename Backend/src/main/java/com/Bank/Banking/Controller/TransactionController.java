package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.TransactionDTO.DepositRequest;
import com.Bank.Banking.DTO.TransactionDTO.TransactionResponse;
import com.Bank.Banking.DTO.TransactionDTO.TransferRequest;
import com.Bank.Banking.DTO.TransactionDTO.WithdrawRequest;
import com.Bank.Banking.Service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        TransactionResponse response = transactionService.withdraw(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = transactionService.transfer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{referenceNumber}")
    public ResponseEntity<TransactionResponse> getTransactionByReference(@PathVariable String referenceNumber) {
        TransactionResponse response = transactionService.getTransactionByReference(referenceNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(@PathVariable String accountNumber) {
        List<TransactionResponse> response = transactionService.getAccountTransactions(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountNumber}/mini-statement")
    public ResponseEntity<List<TransactionResponse>> getMiniStatement(@PathVariable String accountNumber) {
        List<TransactionResponse> response = transactionService.getMiniStatement(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountNumber}/between-dates")
    public ResponseEntity<List<TransactionResponse>> getTransactionsBetweenDates(
            @PathVariable String accountNumber,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<TransactionResponse> response = transactionService.getTransactionsBetweenDates(accountNumber, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/statement/pdf", produces = "application/pdf")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<byte[]> getMyTransactionStatementPdf() {
        byte[] pdfBytes = transactionService.getMyTransactionStatementPdf();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=transaction-statement.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdfBytes);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> response = transactionService.getAllTransactions();
        return ResponseEntity.ok(response);
    }
}
