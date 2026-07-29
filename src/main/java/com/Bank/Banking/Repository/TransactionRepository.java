package com.Bank.Banking.Repository;

import com.Bank.Banking.Entity.BankAccount;
import com.Bank.Banking.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // You can add custom query methods here if needed
    Transaction findByTransactionReference(String transactionReference);

    Iterable<Transaction> findByFromAccount_AccountNumber(String accountNumber);


    Iterable<Transaction> findByToAccount_AccountNumber(String accountNumber);

    Iterable<Transaction> findTop10ByFromAccount_AccountNumberOrderByTransactionDateDesc(String accountNumber);

    Iterable<Transaction> findByTransactionDateBetween(Instant start, Instant end);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.fromAccount.accountNumber = :accountNumber " +
            "OR t.toAccount.accountNumber = :accountNumber) " +
            "AND t.transactionDate BETWEEN :start AND :end " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountNumberAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("start") Instant start,
            @Param("end") Instant end);
}
