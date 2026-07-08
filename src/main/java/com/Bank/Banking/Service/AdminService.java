package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.EmployeeDTO.EmployeeRequest;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;


import java.util.List;

public interface EmployeeService {

    EmployeeResponse getEmployeeById(Long employeeId);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getEmployeesByDepartment(String department);

    List<EmployeeResponse> getEmployeesByBranch(String branch);

    List<EmployeeResponse> getEmployeesByDesignation(String designation);


    List<EmployeeResponse> getActiveEmployees(boolean isActive);

    EmployeeResponse getMyProfile();

    EmployeeResponse updateMyProfile(EmployeeRequest request);

    List<EmployeeResponse> searchEmployees(String keyword);
}