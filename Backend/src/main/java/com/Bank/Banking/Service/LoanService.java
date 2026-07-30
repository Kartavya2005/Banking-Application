package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.LoanDTO.ApplyLoanRequest;
import com.Bank.Banking.DTO.LoanDTO.LoanResponse;
import com.Bank.Banking.DTO.LoanDTO.UpdateLoanStatusRequest;

import java.util.List;

public interface LoanService {
    // Customer
    LoanResponse applyLoan(ApplyLoanRequest request);

    List<LoanResponse> getMyLoans();

    LoanResponse getMyLoanById(Long loanId);

    // Admin
    List<LoanResponse> getAllLoans();

    LoanResponse getLoanById(Long loanId);

    List<LoanResponse> getLoansByStatus(String status);

    List<LoanResponse> getLoansByType(String type);

    LoanResponse updateLoanStatus(Long loanId, UpdateLoanStatusRequest request);
}
