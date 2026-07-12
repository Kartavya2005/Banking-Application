package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.AdminDTO.AdminCreateEmployeeRequest;
import com.Bank.Banking.DTO.AdminDTO.AdminUpdateEmployeeRequest;
import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;
import com.Bank.Banking.Entity.Employee;
import com.Bank.Banking.Enum.Branch;

import java.util.List;

public interface AdminService {

    Employee createEmployees(AdminCreateEmployeeRequest request);

    Employee updateEmployees(Long employeeId, AdminUpdateEmployeeRequest request);

    Employee getEmployeeById(Long employeeId);

    List<EmployeeResponse> getAllEmployees(String firstName, String email, String phoneNumber, String department, String address);

    List<EmployeeResponse> searchEmployees(String keyword);

    void activateEmployee(Long employeeId);

    void deactivateEmployee(Long employeeId);

    void transferEmployee(Long employeeId, Branch newBranch);

    void resetEmployeePassword(Long employeeId, ChangePasswordDTO request);

}