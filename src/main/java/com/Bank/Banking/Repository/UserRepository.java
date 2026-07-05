package com.Bank.Banking.Repository;
//
//import com.Bank.Banking.Entity.AuthUser;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;

import com.Bank.Banking.Entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
//    List<AuthUser> findAll(Sort sort);
    boolean existsByEmail(String email);
}
