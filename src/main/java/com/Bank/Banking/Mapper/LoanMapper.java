package com.Bank.Banking.Mapper;

import com.Bank.Banking.DTO.LoanDTO.LoanResponse;
import com.Bank.Banking.Entity.Loan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoanMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private LoanMapper() {
    }

    public static LoanResponse toResponse(Loan loan) {
        if (loan == null) {
            return null;
        }

        Double loanAmount = loan.getLoanAmount() != null ? loan.getLoanAmount().doubleValue() : null;

        LocalDate estimatedClosureDate = loan.getApplicationDate() != null && loan.getTenureInMonths() != null
                ? loan.getApplicationDate().plusMonths(loan.getTenureInMonths())
                : null;

        return LoanResponse.builder()
                .loanId(loan.getLoanId())
                .accountNumber(loan.getCustomer() != null && loan.getCustomer().getBankAccounts() != null
                        && !loan.getCustomer().getBankAccounts().isEmpty()
                        ? loan.getCustomer().getBankAccounts().get(0).getAccountNumber()
                        : null)
                .monthlyEMI(calculateMonthlyEmi(loanAmount, loan.getInterestRate(), loan.getTenureInMonths()))
                .closureDate(loan.getClosingDate() != null
                        ? loan.getClosingDate().format(DATE_FORMATTER)
                        : estimatedClosureDate != null ? estimatedClosureDate.format(DATE_FORMATTER) : null)
                .loanType(loan.getLoanType())
                .loanAmount(loanAmount)
                .interestRate(loan.getInterestRate())
                .tenureInMonths(loan.getTenureInMonths())
                .loanStatus(loan.getLoanStatus())
                .applicationDate(loan.getApplicationDate())
                .approvalDate(loan.getApprovalDate())
                .closingDate(loan.getClosingDate())
                .remarks(loan.getRemarks())
                .build();
    }

    private static Double calculateMonthlyEmi(Double principal, Double annualInterestRate, Integer tenureInMonths) {
        if (principal == null || tenureInMonths == null || tenureInMonths <= 0) {
            return null;
        }

        if (annualInterestRate == null || annualInterestRate <= 0) {
            return round(principal / tenureInMonths);
        }

        double monthlyRate = annualInterestRate / 12.0 / 100.0;
        double factor = Math.pow(1 + monthlyRate, tenureInMonths);
        double emi = principal * monthlyRate * factor / (factor - 1);
        return round(emi);
    }

    private static Double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}


