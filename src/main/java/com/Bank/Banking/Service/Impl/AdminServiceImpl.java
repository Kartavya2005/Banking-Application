package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.EmployeeDTO.EmployeeRequest;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;
import com.Bank.Banking.Entity.Employee;
import com.Bank.Banking.Repository.EmployeeRepository;
import com.Bank.Banking.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request){

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .branch(request.getBranch())
                .joiningDate(request.getJoiningDate())
                .salary(request.getSalary())
                .isActive(request.isActive())
                .address(request.getAddress())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully: {}", savedEmployee.getEmployeeId());
        return mapToEmployeeResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse updateMyProfile(EmployeeRequest request) {
       Employee employee = Employee.builder().
                firstName(request.getFirstName())
                .lastName(request.getLastName())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .branch(request.getBranch())
                .joiningDate(request.getJoiningDate())
                .salary(request.getSalary())
                .isActive(request.isActive())
                .address(request.getAddress())
                .build();

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee profile updated successfully: {}", updatedEmployee.getEmployeeId());
        return mapToEmployeeResponse(updatedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", employeeId);
                    return new RuntimeException("Employee not found");
                });
        log.info("Employee retrieved successfully: {}", employeeId);
        return mapToEmployeeResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByBranch(String branch) {
        return employeeRepository.findByBranch(branch).stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDesignation(String designation) {
        return employeeRepository.findByDesignation(designation).stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getActiveEmployees(boolean isActive) {
        return employeeRepository.findByisActive(isActive).stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }




    @Override
    public EmployeeResponse getMyProfile() {
        // This method would typically get the currently authenticated user's employee profile.
        // For now, returning a placeholder or throwing an exception.
        throw new UnsupportedOperationException("Not yet implemented: Get current employee profile");
    }


    @Override
    public List<EmployeeResponse> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword).stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .department(employee.getDepartment())
                .designation(employee.getDesignation())
                .branch(employee.getBranch())
                .joiningDate(employee.getJoiningDate())
                .salary(employee.getSalary())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .isActive(employee.isActive())
                .address(employee.getAddress())
                .build();
    }
}
