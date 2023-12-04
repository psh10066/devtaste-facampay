package com.devtaste.facampay.domain.model.payment;

import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.infrastructure.helper.PaymentTestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentRepositoryTest extends PaymentTestHelper {

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeAll
    void before() {
        super.preparePaymentTest();
    }

    @DisplayName("결제 저장")
    @Test
    void save() {
        long beforeCount = paymentRepository.count();

        Payment payment = paymentRepository.save(Payment.of(super.getStore(), super.getUser(), 10000L, PaymentStatusType.WAITING));

        long afterCount = paymentRepository.count();

        assertNotNull(payment.getPaymentId());
        assertEquals(beforeCount + 1, afterCount);
    }
}