package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeRequest;
import com.Bank.Banking.DTO.EmployeeDTO.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse getMyProfile();

    EmployeeResponse updateMyProfile(EmployeeRequest request);

    void changePassword(ChangePasswordDTO request);
}
