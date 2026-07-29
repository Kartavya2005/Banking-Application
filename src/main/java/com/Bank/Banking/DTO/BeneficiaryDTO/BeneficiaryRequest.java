package com.Bank.Banking.DTO.BeneficiaryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaryRequest {
    private String nickname;
    private Long beneficiaryAccountId;
}

