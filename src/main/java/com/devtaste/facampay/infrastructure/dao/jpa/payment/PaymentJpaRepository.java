package com.devtaste.facampay.infrastructure.dao.jpa.payment;

import com.devtaste.facampay.domain.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findFirstByStoreStoreIdAndUserUserIdOrderByPaymentIdDesc(Long storeId, Long userId);
}
