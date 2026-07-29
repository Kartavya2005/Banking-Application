package com.Bank.Banking.Repository;

import com.Bank.Banking.Entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByCustomerCustomerId(Long customerId);

    Optional<Beneficiary> findByBeneficiaryIdAndCustomerCustomerId(Long beneficiaryId, Long customerId);

    Optional<Beneficiary> findByNicknameAndCustomerCustomerId(String nickname, Long customerId);

    boolean existsByBeneficiaryIdAndCustomerCustomerId(Long beneficiaryId, Long customerId);
}
