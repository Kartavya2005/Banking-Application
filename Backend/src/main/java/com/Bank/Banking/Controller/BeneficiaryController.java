package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryRequest;
import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryResponse;
import com.Bank.Banking.DTO.BeneficiaryDTO.BeneficiaryUpdateRequest;
import com.Bank.Banking.Service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> addBeneficiary(@RequestBody BeneficiaryRequest request) {
        BeneficiaryResponse response = beneficiaryService.addBeneficiary(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getAllBeneficiaries() {
        List<BeneficiaryResponse> beneficiaries = beneficiaryService.getAllBeneficiaries();
        return ResponseEntity.ok(beneficiaries);
    }

    @GetMapping("/{beneficiaryId}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiaryById(@PathVariable Long beneficiaryId) {
        BeneficiaryResponse response = beneficiaryService.getBeneficiaryById(beneficiaryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiaryByNickname(@PathVariable String nickname) {
        BeneficiaryResponse response = beneficiaryService.getBeneficiaryByNickname(nickname);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{beneficiaryId}")
    public ResponseEntity<BeneficiaryResponse> updateBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestBody BeneficiaryUpdateRequest request) {
        BeneficiaryResponse response = beneficiaryService.updateBeneficiary(beneficiaryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long beneficiaryId) {
        beneficiaryService.deleteBeneficiary(beneficiaryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{beneficiaryId}/verify")
    public ResponseEntity<Boolean> verifyBeneficiaryOwner(@PathVariable Long beneficiaryId) {
        boolean isOwner = beneficiaryService.isBeneficiaryOwner(beneficiaryId);
        return ResponseEntity.ok(isOwner);
    }
}

