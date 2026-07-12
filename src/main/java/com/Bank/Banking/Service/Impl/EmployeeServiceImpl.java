package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeRequest;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;
import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Entity.Employee;
import com.Bank.Banking.Mapper.EmployeeMapper;
import com.Bank.Banking.Repository.EmployeeRepository;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private Employee getLoggedInEmployee() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return employeeRepository.findByUserEmail(email) // Corrected method name
                .orElseThrow(() ->
                        new RuntimeException("Employee not found for email: " + email));
    }



    @Override
    public EmployeeResponse getMyProfile() {

        log.info("Fetching logged-in employee profile");

        Employee employee = getLoggedInEmployee();

        return EmployeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse updateMyProfile(EmployeeRequest request) {

        Employee employee = getLoggedInEmployee();

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setAddress(request.getAddress());

        Employee updatedEmployee = employeeRepository.save(employee);

        log.info("Employee profile updated successfully: {}",
                updatedEmployee.getUser().getEmail());

        return EmployeeMapper.toResponse(updatedEmployee);
    }

    @Override
    public void changePassword(ChangePasswordDTO request) {

        Employee employee = getLoggedInEmployee();

        AuthUser user = employee.getUser();

        // Check current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new RuntimeException("Current password is incorrect.");
        }

        // Check new password and confirm password
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new RuntimeException("Passwords do not match.");
        }

        // Encode new password
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        log.info("Password changed successfully for {}",
                user.getEmail());
    }
}
