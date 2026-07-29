package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryRequest;
import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryResponse;
import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryUpdateRequest;

import java.util.List;

public interface BeneficiaryService {

    // Add a new beneficiary
    BeneficiaryResponse addBeneficiary(BeneficiaryRequest request);

    // Get all beneficiaries for the logged-in customer
    List<BeneficiaryResponse> getAllBeneficiaries();

    // Get a specific beneficiary by ID (for logged-in customer)
    BeneficiaryResponse getBeneficiaryById(Long beneficiaryId);

    // Get a specific beneficiary by nickname (for logged-in customer)
    BeneficiaryResponse getBeneficiaryByNickname(String nickname);

    // Update a beneficiary's nickname
    BeneficiaryResponse updateBeneficiary(Long beneficiaryId, BeneficiaryUpdateRequest request);

    // Delete a beneficiary
    void deleteBeneficiary(Long beneficiaryId);

    // Check if beneficiary exists for logged-in customer
    boolean isBeneficiaryOwner(Long beneficiaryId);
}

