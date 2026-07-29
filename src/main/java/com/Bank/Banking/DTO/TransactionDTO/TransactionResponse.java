package com.Bank.Banking.DTO.TransactionDTO;

import com.Bank.Banking.Enum.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String transactionReference;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String description;
    private Instant transactionDate;

}
