package com.devtaste.facampay.infrastructure.dao.jpa.payment;

import com.devtaste.facampay.domain.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
