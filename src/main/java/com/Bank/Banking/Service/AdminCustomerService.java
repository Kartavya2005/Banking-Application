package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.AuthDTO.ChangePasswordDTO;
import com.Bank.Banking.DTO.AuthDTO.RegisterRequest;
import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.DTO.CustomerDTO.UpdateCustomerRequest;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Enum.Branch;

import java.util.List;

public interface AdminCustomerService {
    CustomerResponse createCustomer(RegisterRequest request);

    CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request);

    CustomerResponse getCustomerById(Long customerId);

    List<CustomerResponse> getAllCustomers();

    List<CustomerResponse> searchCustomers(String keyword);

    void resetCustomerPassword(Long customerId, ChangePasswordDTO request);

    void approveKyc(Long customerId);

    void rejectKyc(Long customerId);
}
