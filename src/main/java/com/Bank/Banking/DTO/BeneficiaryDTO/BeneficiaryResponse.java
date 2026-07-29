package com.Bank.Banking.DTO.BeneficiaryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaryResponse {
    private Long beneficiaryId;
    private String nickname;
    private String beneficiaryAccountNumber;
    private String beneficiaryAccountHolderName;
    private Instant createdAt;
}

