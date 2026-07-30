package com.Bank.Banking.Entity;

import com.Bank.Banking.Enum.LoanStatus;
import com.Bank.Banking.Enum.LoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType;

    @Column(nullable = false)
    private BigDecimal loanAmount;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Integer tenureInMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus loanStatus;

    @Column(nullable = false)
    private LocalDate applicationDate;

    private LocalDate approvalDate;

    private LocalDate closingDate;

    private String remarks;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
