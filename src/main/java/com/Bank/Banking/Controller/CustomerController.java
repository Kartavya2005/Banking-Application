package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.DTO.CustomerDTO.UpdateCustomerRequest;
import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/profile")
    public ResponseEntity<CustomerResponse> getMyProfile(){
        return ResponseEntity.ok(customerService.getMyProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<CustomerResponse> updateMyProfile(@Valid @RequestBody UpdateCustomerRequest request){
        return ResponseEntity.ok(customerService.updateProfile(request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDTO request) {

        customerService.changePassword(request);

        return ResponseEntity.ok("Password changed successfully.");
    }
}
