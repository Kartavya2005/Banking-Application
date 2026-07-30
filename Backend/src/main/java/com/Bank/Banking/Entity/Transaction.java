package com.Bank.Banking.Entity;

import com.Bank.Banking.Enum.TransactionStatus;
import com.Bank.Banking.Enum.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private String transactionReference;

    private String description;

    @CreationTimestamp
    private Instant transactionDate;

    @Column(nullable = false)
    private BigDecimal amount;

    private BigDecimal balanceAfterTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private BankAccount fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private BankAccount toAccount;
}
