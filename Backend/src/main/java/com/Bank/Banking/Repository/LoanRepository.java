package com.Bank.Banking.Repository;

import com.Bank.Banking.Entity.Loan;
import com.Bank.Banking.Enum.LoanStatus;
import com.Bank.Banking.Enum.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    // Customer
    List<Loan> findByCustomerCustomerId(Long customerId);

    Optional<Loan> findByLoanIdAndCustomerCustomerId(Long loanId, Long customerId);

    // Admin
    List<Loan> findByLoanStatus(LoanStatus loanStatus);

    List<Loan> findByLoanType(LoanType loanType);

    List<Loan> findByCustomerCustomerIdAndLoanStatus(Long customerId, LoanStatus loanStatus);

    List<Loan> findByCustomerCustomerIdAndLoanType(Long customerId, LoanType loanType);
}
