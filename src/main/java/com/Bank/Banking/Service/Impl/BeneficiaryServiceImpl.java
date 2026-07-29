package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryRequest;
import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryResponse;
import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryUpdateRequest;
import com.Bank.Banking.Entity.BankAccount;
import com.Bank.Banking.Entity.Beneficiary;
import com.Bank.Banking.Entity.Customer;
import com.Bank.Banking.Exception.AccountNotFoundException;
import com.Bank.Banking.Exception.CustomerNotFoundException;
import com.Bank.Banking.Repository.BankAccountRepository;
import com.Bank.Banking.Repository.BeneficiaryRepository;
import com.Bank.Banking.Repository.CustomerRepository;
import com.Bank.Banking.Service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;

    private Customer getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomerNotFoundException("Customer not found.");
        }

        String email = authentication.getName();

        return customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found."));
    }

    private BeneficiaryResponse mapToBeneficiaryResponse(Beneficiary beneficiary) {
        BankAccount account = beneficiary.getBeneficiaryAccount();
        Customer accountOwner = account.getCustomer();
        String accountHolderName = (accountOwner.getFirstName() != null ? accountOwner.getFirstName() : "")
                + " " + (accountOwner.getLastName() != null ? accountOwner.getLastName() : "");

        return BeneficiaryResponse.builder()
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .nickname(beneficiary.getNickname())
                .beneficiaryAccountNumber(account.getAccountNumber())
                .beneficiaryAccountHolderName(accountHolderName.trim())
                .createdAt(beneficiary.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public BeneficiaryResponse addBeneficiary(BeneficiaryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Beneficiary request must not be null.");
        }

        validateBeneficiaryRequest(request);

        Customer customer = getLoggedInCustomer();

        // Check if beneficiary account exists
        BankAccount beneficiaryAccount = bankAccountRepository.findById(request.getBeneficiaryAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Beneficiary account not found."));

        // Check if customer is trying to add their own account as beneficiary
        if (beneficiaryAccount.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new IllegalArgumentException("Cannot add your own account as a beneficiary.");
        }

        // Check if beneficiary already exists
        if (beneficiaryRepository.findByNicknameAndCustomerCustomerId(request.getNickname(), customer.getCustomerId()).isPresent()) {
            throw new IllegalArgumentException("Beneficiary with nickname '" + request.getNickname() + "' already exists.");
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .nickname(request.getNickname())
                .customer(customer)
                .beneficiaryAccount(beneficiaryAccount)
                .createdAt(Instant.now())
                .build();

        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);

        return mapToBeneficiaryResponse(savedBeneficiary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getAllBeneficiaries() {
        Customer customer = getLoggedInCustomer();

        return beneficiaryRepository.findByCustomerCustomerId(customer.getCustomerId())
                .stream()
                .map(this::mapToBeneficiaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryResponse getBeneficiaryById(Long beneficiaryId) {
        if (beneficiaryId == null || beneficiaryId <= 0) {
            throw new IllegalArgumentException("Beneficiary ID must be greater than zero.");
        }

        Customer customer = getLoggedInCustomer();

        Beneficiary beneficiary = beneficiaryRepository.findByBeneficiaryIdAndCustomerCustomerId(beneficiaryId, customer.getCustomerId())
                .orElseThrow(() -> new AccountNotFoundException("Beneficiary not found or does not belong to logged-in customer."));

        return mapToBeneficiaryResponse(beneficiary);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryResponse getBeneficiaryByNickname(String nickname) {
        validateNickname(nickname);

        Customer customer = getLoggedInCustomer();

        Beneficiary beneficiary = beneficiaryRepository.findByNicknameAndCustomerCustomerId(nickname, customer.getCustomerId())
                .orElseThrow(() -> new AccountNotFoundException("Beneficiary with nickname '" + nickname + "' not found."));

        return mapToBeneficiaryResponse(beneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryResponse updateBeneficiary(Long beneficiaryId, BeneficiaryUpdateRequest request) {
        if (beneficiaryId == null || beneficiaryId <= 0) {
            throw new IllegalArgumentException("Beneficiary ID must be greater than zero.");
        }

        if (request == null) {
            throw new IllegalArgumentException("Update request must not be null.");
        }

        validateNickname(request.getNickname());

        Customer customer = getLoggedInCustomer();

        Beneficiary beneficiary = beneficiaryRepository.findByBeneficiaryIdAndCustomerCustomerId(beneficiaryId, customer.getCustomerId())
                .orElseThrow(() -> new AccountNotFoundException("Beneficiary not found or does not belong to logged-in customer."));

        // Check if new nickname is already used by another beneficiary
        beneficiaryRepository.findByNicknameAndCustomerCustomerId(request.getNickname(), customer.getCustomerId())
                .ifPresent(existing -> {
                    if (!existing.getBeneficiaryId().equals(beneficiaryId)) {
                        throw new IllegalArgumentException("Nickname '" + request.getNickname() + "' is already in use.");
                    }
                });

        beneficiary.setNickname(request.getNickname());

        Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary);

        return mapToBeneficiaryResponse(updatedBeneficiary);
    }

    @Override
    @Transactional
    public void deleteBeneficiary(Long beneficiaryId) {
        if (beneficiaryId == null || beneficiaryId <= 0) {
            throw new IllegalArgumentException("Beneficiary ID must be greater than zero.");
        }

        Customer customer = getLoggedInCustomer();

        if (!beneficiaryRepository.existsByBeneficiaryIdAndCustomerCustomerId(beneficiaryId, customer.getCustomerId())) {
            throw new AccountNotFoundException("Beneficiary not found or does not belong to logged-in customer.");
        }

        beneficiaryRepository.deleteById(beneficiaryId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBeneficiaryOwner(Long beneficiaryId) {
        if (beneficiaryId == null || beneficiaryId <= 0) {
            return false;
        }

        try {
            Customer customer = getLoggedInCustomer();
            return beneficiaryRepository.existsByBeneficiaryIdAndCustomerCustomerId(beneficiaryId, customer.getCustomerId());
        } catch (CustomerNotFoundException e) {
            return false;
        }
    }

    private void validateBeneficiaryRequest(BeneficiaryRequest request) {
        validateNickname(request.getNickname());

        if (request.getBeneficiaryAccountId() == null || request.getBeneficiaryAccountId() <= 0) {
            throw new IllegalArgumentException("Beneficiary account ID must be greater than zero.");
        }
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname must not be blank.");
        }

        if (nickname.length() > 100) {
            throw new IllegalArgumentException("Nickname must not exceed 100 characters.");
        }
    }
}


