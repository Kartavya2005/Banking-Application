package com.Bank.Banking.Repository;
//
//import com.Bank.Banking.Entity.AuthUser;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;

import com.Bank.Banking.Entity.AuthUser;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email);
//    List<AuthUser> findAll(Sort sort);
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(@NotBlank(message = "PhoneNumber is required") String phoneNumber);
}
