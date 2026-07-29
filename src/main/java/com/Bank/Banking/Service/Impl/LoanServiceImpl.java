package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.LoanDTO.ApplyLoanRequest;
import com.Bank.Banking.DTO.LoanDTO.LoanResponse;
import com.Bank.Banking.DTO.LoanDTO.UpdateLoanStatusRequest;
import com.Bank.Banking.Entity.BankAccount;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Entity.Loan;
import com.Bank.Banking.Enum.AccountStatus;
import com.Bank.Banking.Enum.LoanStatus;
import com.Bank.Banking.Enum.LoanType;
import com.Bank.Banking.Mapper.LoanMapper;
import com.Bank.Banking.Repository.BankAccountRepository;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Repository.LoanRepository;
import com.Bank.Banking.Service.NotificationService;
import com.Bank.Banking.Service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanServiceImpl implements LoanService {

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final BankAccountRepository bankAccountRepository;
    private final NotificationService notificationService;


    public Customer getLoggedInCustomer() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return customerRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found for email: " + email));
    }

    private LoanResponse toResponse(Loan loan) {
        return LoanMapper.toResponse(loan);
    }

    private double resolveInterestRate(LoanType loanType) {
        return switch (loanType) {
            case PERSONAL -> 12.0;
            case HOME -> 8.5;
            case CAR -> 9.5;
            case EDUCATION -> 7.0;
        };
    }

    private LoanType parseLoanType(String type) {
        try {
            return LoanType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new RuntimeException("Invalid loan type: " + type);
        }
    }

    private LoanStatus parseLoanStatus(String status) {
        try {
            return LoanStatus.valueOf(status.toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new RuntimeException("Invalid loan status: " + status);
        }
    }

    private Loan getLoanForLoggedInCustomer(Long loanId) {
        Customer customer = getLoggedInCustomer();
        return loanRepository.findByLoanIdAndCustomerCustomerId(loanId, customer.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));
    }

    @Override
    public LoanResponse applyLoan(ApplyLoanRequest request) {
        Customer customer = getLoggedInCustomer();
        BankAccount bankAccount = bankAccountRepository.findByAccountNumberAndCustomerCustomerId(
                        request.getAccountNumber(),
                        customer.getCustomerId())
                .orElseThrow(() -> new RuntimeException(
                        "Bank account not found for account number: " + request.getAccountNumber()));

        if (bankAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Loan can only be applied using an active bank account.");
        }

        Loan loan = Loan.builder()
                .customer(customer)
                .loanType(request.getLoanType())
                .loanAmount(BigDecimal.valueOf(request.getLoanAmount()))
                .interestRate(resolveInterestRate(request.getLoanType()))
                .tenureInMonths(request.getTenureInMonths())
                .loanStatus(LoanStatus.PENDING)
                .applicationDate(LocalDate.now())
                .remarks(request.getRemarks())
                .build();

        Loan savedLoan = loanRepository.save(loan);
        log.info("Loan application submitted successfully for customer {} via account {}", customer.getCustomerId(), bankAccount.getAccountNumber());
        notificationService.notifyUser(
                customer.getUser().getId(),
                "Loan application submitted",
                "Your loan application has been submitted successfully and is now pending review.",
                "LOAN",
                String.valueOf(savedLoan.getLoanId())
        );
        notificationService.notifyRole(
                com.Bank.Banking.Enum.Role.ADMIN,
                "New loan application",
                "A new loan application is pending review.",
                "LOAN",
                String.valueOf(savedLoan.getLoanId())
        );
        return toResponse(savedLoan);
    }

    @Override
    public List<LoanResponse> getMyLoans() {
        Customer customer = getLoggedInCustomer();
        return loanRepository.findByCustomerCustomerId(customer.getCustomerId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LoanResponse getMyLoanById(Long loanId) {
        return toResponse(getLoanForLoggedInCustomer(loanId));
    }

    @Override
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LoanResponse getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));
        return toResponse(loan);
    }

    @Override
    public List<LoanResponse> getLoansByStatus(String status) {
        LoanStatus loanStatus = parseLoanStatus(status);
        return loanRepository.findByLoanStatus(loanStatus)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<LoanResponse> getLoansByType(String type) {
        LoanType loanType = parseLoanType(type);
        return loanRepository.findByLoanType(loanType)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LoanResponse updateLoanStatus(Long loanId, UpdateLoanStatusRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));

        loan.setLoanStatus(request.getLoanStatus());
        if (request.getRemarks() != null && !request.getRemarks().isBlank()) {
            loan.setRemarks(request.getRemarks());
        }

        if (request.getLoanStatus() == LoanStatus.APPROVED) {
            loan.setApprovalDate(LocalDate.now());
        }
        if (request.getLoanStatus() == LoanStatus.CLOSED) {
            loan.setClosingDate(LocalDate.now());
        }

        Loan savedLoan = loanRepository.save(loan);
        notificationService.notifyUser(
                loan.getCustomer().getUser().getId(),
                "Loan status updated",
                "Your loan status has been updated to " + request.getLoanStatus() + ".",
                "LOAN",
                String.valueOf(savedLoan.getLoanId())
        );
        return toResponse(savedLoan);
    }
}
