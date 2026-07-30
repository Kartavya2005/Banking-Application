package com.Bank.Banking.DTO.TransactionDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawRequest {
    private String accountNumber;
    private BigDecimal amount;
    private String description;
}
