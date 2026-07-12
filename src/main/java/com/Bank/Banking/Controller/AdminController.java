package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.AdminDTO.AdminCreateEmployeeRequest;
import com.Bank.Banking.DTO.AdminDTO.AdminUpdateEmployeeRequest;
import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;
import com.Bank.Banking.Entity.Employee;
import com.Bank.Banking.Enum.Branch;
import com.Bank.Banking.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create-employee")
    public ResponseEntity<Employee> createEmployees(@Valid @RequestBody AdminCreateEmployeeRequest request) {
        Employee createEmployee = adminService.createEmployees(request);
        return new ResponseEntity<>(createEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployees(@PathVariable("employeeId") Long employeeId, @Valid @RequestBody  AdminUpdateEmployeeRequest request) {
        Employee updateEmployee = adminService.updateEmployees(employeeId,request);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                adminService.getEmployeeById(employeeId)
        );
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String address) {

        return ResponseEntity.ok(
                adminService.getAllEmployees(firstName, email, phoneNumber, department, address)
        );
    }

    @GetMapping("/employees/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam String keyword){

        return ResponseEntity.ok(
                adminService.searchEmployees(keyword)
        );
    }

    @PutMapping("/employees/{employeeId}/activate")
    public ResponseEntity<String> activateEmployee(
            @PathVariable Long employeeId) {

        adminService.activateEmployee(employeeId);

        return ResponseEntity.ok("Employee activated successfully.");
    }

    /**
     * Deactivate Employee
     */
    @PutMapping("/employees/{employeeId}/deactivate")
    public ResponseEntity<String> deactivateEmployee(
            @PathVariable Long employeeId) {

        adminService.deactivateEmployee(employeeId);

        return ResponseEntity.ok("Employee deactivated successfully.");
    }

    /**
     * Transfer Employee to another Branch
     */
    @PutMapping("/employees/{employeeId}/transfer")
    public ResponseEntity<String> transferEmployee(
            @PathVariable Long employeeId,
            @RequestParam Branch newBranch) {

        adminService.transferEmployee(employeeId, newBranch);

        return ResponseEntity.ok("Employee transferred successfully.");
    }

    @PutMapping("/employees/{employeeId}/reset-password")
    public ResponseEntity<String> resetEmployeePassword(
            @PathVariable Long employeeId,
            @Valid @RequestBody ChangePasswordDTO request) {

        adminService.resetEmployeePassword(employeeId, request);

        return ResponseEntity.ok("Password reset successfully.");
    }


}
