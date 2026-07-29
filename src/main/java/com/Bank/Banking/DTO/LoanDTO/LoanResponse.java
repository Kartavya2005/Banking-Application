package com.Bank.Banking.DTO.LoanDTO;

import com.Bank.Banking.Enum.LoanStatus;
import com.Bank.Banking.Enum.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponse {

    private Long loanId;
    private String accountNumber;
    private Double monthlyEMI;

    private String closureDate;

    private LoanType loanType;

    private Double loanAmount;

    private Double interestRate;

    private Integer tenureInMonths;

    private LoanStatus loanStatus;

    private LocalDate applicationDate;

    private LocalDate approvalDate;

    private LocalDate closingDate;

    private String remarks;

}
