package com.Bank.Banking.Mapper;

import com.Bank.Banking.DTO.CustomerDTO.CustomerResponse;
import com.Bank.Banking.Entity.Customer;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .email(customer.getUser().getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .build();
    }
}
