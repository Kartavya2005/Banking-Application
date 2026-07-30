package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.AdminDTO.AdminCreateEmployeeRequest;
import com.Bank.Banking.DTO.AdminDTO.AdminUpdateEmployeeRequest;
import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;
import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Entity.Employee;
import com.Bank.Banking.Enum.Branch;
import com.Bank.Banking.Mapper.EmployeeMapper;
import com.Bank.Banking.Repository.EmployeeRepository;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final EmployeeRepository employeeRepository;


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    @Override
    public Employee createEmployees(AdminCreateEmployeeRequest request) {
        AuthUser user = new AuthUser();          // create AuthUser first
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEnabled(true);                   // set enabled so they can log in
        userRepository.save(user);               // save it first (if not cascaded)

        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setBranch(request.getBranch());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setSalary(request.getSalary());
        employee.setActive(request.isActive());
        employee.setAddress(request.getAddress());
        employee.setUser(user);                  // link the user to the employee

        return employeeRepository.save(employee);

    }

    @Override
    public Employee updateEmployees(Long employeeId, AdminUpdateEmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        existingEmployee.getUser().setRole(request.getRole());
// safer:
        if (existingEmployee.getUser() != null) {
            existingEmployee.getUser().setRole(request.getRole());
        }
        existingEmployee.setFirstName(request.getFirstName());
        existingEmployee.setLastName(request.getLastName());
        existingEmployee.getUser().setRole(request.getRole());
        existingEmployee.getUser().setPhoneNumber(request.getPhoneNumber());
        existingEmployee.setDepartment(request.getDepartment());
        existingEmployee.setDesignation(request.getDesignation());
        existingEmployee.setBranch(request.getBranch());
        existingEmployee.setSalary(request.getSalary());
        existingEmployee.setActive(request.isActive());
        existingEmployee.setAddress(request.getAddress());
        return employeeRepository.save(existingEmployee);

    }

    @Override
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
    }

    @Override
    public List<EmployeeResponse> getAllEmployees(String firstName,
                                                  String email,
                                                  String phoneNumber,
                                                  String department,
                                                  String address) {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword).stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void activateEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new RuntimeException("Employee not found with id: " + employeeId));
        employee.setActive(true);
        employeeRepository.save(employee);
    }

    @Override
    public void deactivateEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new RuntimeException("Employee not found with id: " + employeeId));
        employee.setActive(false);
        employee.getUser().setEnabled(false);
        employeeRepository.save(employee);

    }

    @Override
    public void transferEmployee(Long employeeId, Branch newBranch) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new RuntimeException("Employee not found with id: "+ employeeId));
        employee.setBranch(newBranch);
        employeeRepository.save(employee);
    }

    @Override
    public void resetEmployeePassword(Long employeeId,
                                      ChangePasswordDTO request) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        AuthUser user = employee.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        log.info("Password reset successfully for employee: {}", employeeId);
    }
}
