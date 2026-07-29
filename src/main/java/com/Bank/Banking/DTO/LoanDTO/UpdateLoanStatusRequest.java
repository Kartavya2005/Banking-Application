package com.Bank.Banking.DTO.LoanDTO;


import com.Bank.Banking.Enum.LoanStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLoanStatusRequest {
    private Long loanId;
    @NotNull(message = "Loan status is required")
    private LoanStatus loanStatus;
    private String remarks;
}
