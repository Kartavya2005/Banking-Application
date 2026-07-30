package com.Bank.Banking.DTO.LoanDTO;


import com.Bank.Banking.Enum.LoanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyLoanRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be greater than 0")
    private Double loanAmount;

    @NotNull(message = "Tenure is required")
    @Positive(message = "Tenure must be greater than 0")
    private Integer tenureInMonths;

    private String remarks;

}
