package com.Bank.Banking.Controller;


import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.AuthDTO.RegisterRequest;
import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.DTO.CustomerDTO.UpdateCustomerRequest;
import com.Bank.Banking.Service.AdminCustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-customer")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody RegisterRequest request) {

        CustomerResponse response = adminCustomerService.createCustomer(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerRequest request) {

        CustomerResponse response = adminCustomerService.updateCustomer(customerId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId){
        CustomerResponse response = adminCustomerService.getCustomerById(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        List<CustomerResponse> response = adminCustomerService.getAllCustomers();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/customers/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(
            @RequestParam String keyword){
        List<CustomerResponse> response = adminCustomerService.searchCustomers(keyword);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/customers/{customerId}/reset-password")
    public ResponseEntity<String> resetCustomerPassword(
            @PathVariable Long customerId,
            @Valid @RequestBody ChangePasswordDTO request){
        adminCustomerService.resetCustomerPassword(customerId, request);
        return ResponseEntity.ok("Customer password reset successfully.");
    }

    @PutMapping("/customers/{customerId}/approve-kyc")
    public ResponseEntity<String> approveKyc(@PathVariable Long customerId) {
        adminCustomerService.approveKyc(customerId);
        return ResponseEntity.ok("Customer KYC approved successfully.");
    }

    @PutMapping("/customers/{customerId}/reject-kyc")
    public ResponseEntity<String> rejectKyc(@PathVariable Long customerId) {
        adminCustomerService.rejectKyc(customerId);
        return ResponseEntity.ok("Customer KYC rejected successfully.");
    }

}
