package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.LoanDTO.ApplyLoanRequest;
import com.Bank.Banking.DTO.LoanDTO.LoanResponse;
import com.Bank.Banking.DTO.LoanDTO.UpdateLoanStatusRequest;
import com.Bank.Banking.Service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<LoanResponse> applyLoan(@Valid @RequestBody ApplyLoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.applyLoan(request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<LoanResponse>> getMyLoans() {
        return ResponseEntity.ok(loanService.getMyLoans());
    }

    @GetMapping("/my/{loanId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<LoanResponse> getMyLoanById(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getMyLoanById(loanId));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<LoanResponse>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/admin/{loanId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }

    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<LoanResponse>> getLoansByStatus(@PathVariable String status) {
        return ResponseEntity.ok(loanService.getLoansByStatus(status));
    }

    @GetMapping("/admin/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<LoanResponse>> getLoansByType(@PathVariable String type) {
        return ResponseEntity.ok(loanService.getLoansByType(type));
    }

    @PutMapping("/admin/{loanId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> updateLoanStatus(
            @PathVariable Long loanId,
            @Valid @RequestBody UpdateLoanStatusRequest request) {
        return ResponseEntity.ok(loanService.updateLoanStatus(loanId, request));
    }
}

