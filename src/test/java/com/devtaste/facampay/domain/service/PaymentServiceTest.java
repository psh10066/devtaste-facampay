package com.devtaste.facampay.domain.service;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.infrastructure.helper.PaymentTestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentServiceTest extends PaymentTestHelper {

    @Autowired
    private PaymentRepository paymentRepository;

    private PaymentService paymentService;

    @BeforeAll
    void before() {
        super.preparePaymentTest();

        paymentService = new PaymentService(paymentRepository);
    }

    @DisplayName("결제 저장")
    @Test
    void save() {
        paymentService.save(Payment.builder()
            .store(super.getStore())
            .user(super.getUser())
            .money(10000L)
            .paymentStatus(PaymentStatusType.WAITING)
            .build());
    }
}