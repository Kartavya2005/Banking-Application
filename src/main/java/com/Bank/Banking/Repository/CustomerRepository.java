package com.Bank.Banking.Repository;

import com.Bank.Banking.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserEmail(String email);

//    Optional<Customer> findByPhoneNumber(String phoneNumber);
//
//    Optional<Customer> existsByPhoneNumber(String phoneNumber);
}
