package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeRequest;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;
import com.Bank.Banking.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<EmployeeResponse> getMyProfile(){
        return ResponseEntity.ok(employeeService.getMyProfile());
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<EmployeeResponse> updateMyProfile(@Valid @RequestBody EmployeeRequest request){
        return ResponseEntity.ok(employeeService.updateMyProfile(request));
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDTO request) {

        employeeService.changePassword(request);

        return ResponseEntity.ok("Password changed successfully.");
    }
}
