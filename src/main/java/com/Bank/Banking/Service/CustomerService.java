package com.Bank.Banking.Service;


import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.DTO.CustomerDTO.UpdateCustomerRequest;
import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;

public interface CustomerService {

    CustomerResponse getMyProfile();

    CustomerResponse updateProfile(UpdateCustomerRequest request);

    void changePassword(ChangePasswordDTO request);
}
