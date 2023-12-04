package com.devtaste.facampay.domain.model.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findFirstByStoreStoreIdAndUserUserIdOrderByPaymentIdDesc(Long storeId, Long userId);
}
