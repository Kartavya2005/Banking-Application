package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.TransactionDTO.DepositRequest;
import com.Bank.Banking.DTO.TransactionDTO.TransactionResponse;
import com.Bank.Banking.DTO.TransactionDTO.TransferRequest;
import com.Bank.Banking.DTO.TransactionDTO.WithdrawRequest;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    // Deposit Money
    TransactionResponse deposit(DepositRequest request);

    // Withdraw Money
    TransactionResponse withdraw(WithdrawRequest request);

    // Transfer Money
    TransactionResponse transfer(TransferRequest request);

    // Get Transaction by Reference Number
    TransactionResponse getTransactionByReference(String referenceNumber);

    // Get All Transactions of a Particular Account
    List<TransactionResponse> getAccountTransactions(String accountNumber);

    // Mini Statement (Last 10 Transactions)
    List<TransactionResponse> getMiniStatement(String accountNumber);

    // Get Transactions Between Two Dates
    List<TransactionResponse> getTransactionsBetweenDates(
            String accountNumber,
            LocalDate startDate,
            LocalDate endDate
    );

    // Customer statement PDF for all owned transactions
    byte[] getMyTransactionStatementPdf();

    // Admin - View All Transactions
    List<TransactionResponse> getAllTransactions();
}
