package com.Bank.Banking.Repository;

import com.Bank.Banking.Entity.BankAccount;
import com.Bank.Banking.Enum.AccountStatus;
import com.Bank.Banking.Enum.AccountType;
import com.Bank.Banking.Enum.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    List<BankAccount> findByCustomerCustomerId(Long customerId);

    List<BankAccount> findByCustomerUserEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    List<BankAccount> findByAccountStatus(AccountStatus accountStatus);

    List<BankAccount> findByAccountType(AccountType accountType);

    List<BankAccount> findByBranch(Branch branch);

    Optional<BankAccount> findByAccountNumberAndCustomerCustomerId(
            String accountNumber,
            Long customerId
    );
}
