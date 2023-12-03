package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.service.PaymentService;
import com.devtaste.facampay.infrastructure.helper.PaymentTestHelper;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentApplicationServiceTest extends PaymentTestHelper {

    @Autowired
    private PaymentRepository paymentRepository;

    private PaymentApplicationService paymentApplicationService;

    @BeforeAll
    void before() {
        super.preparePaymentTest();

        PaymentService paymentService = new PaymentService(paymentRepository);
        paymentApplicationService = new PaymentApplicationService(super.getStoreService(), super.getUserService(), paymentService);
    }

    @DisplayName("결제 요청")
    @Test
    void postPayment() {
        PostPaymentRequest request = new PostPaymentRequest(super.getStore().getStoreId(), super.getUser().getUserId(), 10000L);
        paymentApplicationService.postPayment(request);

        Optional<Payment> payment = paymentRepository.findFirstByStoreStoreIdAndUserUserIdOrderByPaymentIdDesc(request.getStoreId(), request.getUserId());

        assertTrue(payment.isPresent());
        assertEquals(payment.get().getMoney(), request.getMoney());
        assertEquals(payment.get().getPaymentStatus(), PaymentStatusType.WAITING);
    }
}