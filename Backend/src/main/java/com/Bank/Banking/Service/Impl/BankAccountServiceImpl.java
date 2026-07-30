package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.BankAccountDTO.BankAccountResponse;
import com.Bank.Banking.DTO.BankAccountDTO.CreateBankAccountRequest;
import com.Bank.Banking.DTO.BankAccountDTO.UpdateBankAccountRequest;
import com.Bank.Banking.Entity.BankAccount;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Enum.AccountStatus;
import com.Bank.Banking.Enum.KYC;
import com.Bank.Banking.Mapper.BankAccountMapper;
import com.Bank.Banking.Repository.BankAccountRepository;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Service.NotificationService;
import com.Bank.Banking.Service.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    public final BankAccountRepository bankAccountRepository;
    public final CustomerRepository customerRepository;
    public final NotificationService notificationService;

    @Override
    public BankAccount createAccount(CreateBankAccountRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (customer.getKyc() != KYC.APPROVED) {
            throw new RuntimeException(
                    "Bank account cannot be created until KYC is approved."
            );
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountType(request.getAccountType());
        bankAccount.setBranch(request.getBranch());
        bankAccount.setBalance(request.getInitialDeposit());
        bankAccount.setCustomer(customer); // Link customer to account
        bankAccount.setAccountStatus(AccountStatus.ACTIVE); // Default status
        bankAccount.setIfsc(request.getBranch().getIfscCode()); // Set IFSC from branch enum
        bankAccount.setCreatedAt(Instant.now());
        bankAccount.setUpdatedAt(Instant.now());
        bankAccount.setAccountNumber(generateUniqueAccountNumber()); // Generate unique account number

        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        notificationService.notifyUser(
                customer.getUser().getId(),
                "Bank account created",
                "Your bank account has been created successfully.",
                "BANK_ACCOUNT",
                String.valueOf(savedBankAccount.getAccountId())
        );
        return savedBankAccount;
    }

    @Override
    public BankAccount updateAccount(Long accountId, UpdateBankAccountRequest request) {
        BankAccount existingBankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank account not found with id: " + accountId));

        if (request.getBranch() != null) {
            existingBankAccount.setBranch(request.getBranch());
            existingBankAccount.setIfsc(request.getBranch().getIfscCode()); // Update IFSC if branch changes
        }
        if (request.getAccountStatus() != null) {
            existingBankAccount.setAccountStatus(request.getAccountStatus());
        }
        existingBankAccount.setUpdatedAt(Instant.now());
        BankAccount savedBankAccount = bankAccountRepository.save(existingBankAccount);
        notificationService.notifyUser(
                savedBankAccount.getCustomer().getUser().getId(),
                "Bank account updated",
                "Your bank account details have been updated.",
                "BANK_ACCOUNT",
                String.valueOf(savedBankAccount.getAccountId())
        );
        return savedBankAccount;
    }

    @Override
    public BankAccountResponse getAccountByNumber(String accountNumber) {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Bank account not found with number: " + accountNumber));
        return BankAccountMapper.toResponse(bankAccount);
    }

    @Override
    public List<BankAccountResponse> getCustomerAccounts(Long customerId) {
        List<BankAccount> accounts = bankAccountRepository.findByCustomerCustomerId(customerId);
        return accounts.stream()
                .map(BankAccountMapper::toResponse)
                .toList();
    }

    @Override
    public List<BankAccountResponse> getMyAccounts() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail;
        if (principal instanceof UserDetails) {
            currentUserEmail = ((UserDetails) principal).getUsername();
        } else {
            currentUserEmail = principal.toString();
        }

        List<BankAccount> accounts = bankAccountRepository.findByCustomerUserEmail(currentUserEmail);
        return accounts.stream()
                .map(BankAccountMapper::toResponse)
                .toList();
    }

    @Override
    public List<BankAccountResponse> getAllAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        return accounts.stream()
                .map(BankAccountMapper::toResponse)
                .toList();
    }

    @Override
    public List<BankAccountResponse> searchAccounts(String keyword) {
        // This is a basic implementation. A more robust search would involve
        // querying by multiple fields (e.g., account holder name, account type, etc.)
        // and potentially using a custom query in the repository.
        try {

            return bankAccountRepository.findByAccountNumber(keyword)
                    .map(BankAccountMapper::toResponse)
                    .map(List::of) // Wrap in a list
                    .orElse(List.of()); // Return empty list if not found
        } catch (NumberFormatException e) {
            log.info("Search keyword '{}' is not a valid account number. Returning empty list.", keyword);
            return List.of();
        }
    }

    @Override
    public void freezeAccount(Long accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank account not found with id: " + accountId));
        bankAccount.setAccountStatus(AccountStatus.INACTIVE);
        bankAccount.setUpdatedAt(Instant.now());
        bankAccountRepository.save(bankAccount);
        log.info("Account with id {} frozen successfully.", accountId);
        notificationService.notifyUser(
                bankAccount.getCustomer().getUser().getId(),
                "Bank account frozen",
                "Your bank account has been frozen.",
                "BANK_ACCOUNT",
                String.valueOf(accountId)
        );
    }

    @Override
    public void unfreezeAccount(Long accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank account not found with id: " + accountId));
        bankAccount.setAccountStatus(AccountStatus.ACTIVE); // Assuming ACTIVE is the default un-frozen state
        bankAccount.setUpdatedAt(Instant.now());
        bankAccountRepository.save(bankAccount);
        log.info("Account with id {} unfrozen successfully.", accountId);
        notificationService.notifyUser(
                bankAccount.getCustomer().getUser().getId(),
                "Bank account unfrozen",
                "Your bank account has been unfrozen.",
                "BANK_ACCOUNT",
                String.valueOf(accountId)
        );
    }

    @Override
    public void closeAccount(Long accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank account not found with id: " + accountId));
        bankAccount.setAccountStatus(AccountStatus.CLOSED);
        bankAccount.setUpdatedAt(Instant.now());
        bankAccountRepository.save(bankAccount);
        log.info("Account with id {} closed successfully.", accountId);
        notificationService.notifyUser(
                bankAccount.getCustomer().getUser().getId(),
                "Bank account closed",
                "Your bank account has been closed.",
                "BANK_ACCOUNT",
                String.valueOf(accountId)
        );
    }

    /**
     * Generates a unique account number.
     * This is a basic placeholder. In a real application,
     * you'd need a more robust, collision-resistant mechanism
     * (e.g., using a sequence generator, UUID, or a dedicated service).
     */
    private String generateUniqueAccountNumber() {
        // Start with epoch milliseconds as a base, converted to String
        String accountNumber = String.valueOf(Instant.now().toEpochMilli());
        // Simple check to avoid immediate collisions if called rapidly.
        // In a high-concurrency scenario, this would still be prone to race conditions.
        // For a more robust solution, consider UUIDs or a dedicated sequence generator.
        while (bankAccountRepository.findByAccountNumber(accountNumber).isPresent()) {
            // If a collision is found, increment the numeric part and convert back to String.
            // This assumes the generated account number is purely numeric.
            try {
                long num = Long.parseLong(accountNumber);
                num++;
                accountNumber = String.valueOf(num);
            } catch (NumberFormatException e) {
                // Should not happen if we start with epoch milli, but good to be defensive.
                // If it happens, generate a completely new one to avoid infinite loop.
                log.warn("Failed to parse account number for increment: {}. Generating new base.", accountNumber);
                accountNumber = String.valueOf(Instant.now().toEpochMilli());
            }
        }
        return accountNumber;
    }
}
