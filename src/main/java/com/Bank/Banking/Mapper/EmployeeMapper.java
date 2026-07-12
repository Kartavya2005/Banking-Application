package com.Bank.Banking.Mapper;

import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;
import com.Bank.Banking.Entity.Employee;

public class EmployeeMapper {

    // private constructor to prevent instantiation of a utility class
    private EmployeeMapper() {
    }

    public static EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .email(employee.getUser().getEmail())
                .role(employee.getUser().getRole())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .department(employee.getDepartment())
                .designation(employee.getDesignation())
                .branch(employee.getBranch())
                .joiningDate(employee.getJoiningDate())
                .salary(employee.getSalary())
                .address(employee.getAddress())
                .isActive(employee.isActive())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}