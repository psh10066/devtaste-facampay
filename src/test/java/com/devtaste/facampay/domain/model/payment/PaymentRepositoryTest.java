package com.devtaste.facampay.domain.model.payment;

import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.infrastructure.helper.RepositoryTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentRepositoryTest extends RepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;

    private Store store;
    private User user;

    @BeforeAll
    void before() {
        this.store = this.storeRepository.findById(1L).orElseThrow();
        this.user = this.userRepository.findById(1L).orElseThrow();
    }

    @DisplayName("결제 저장")
    @Test
    void save() {
        long beforeCount = paymentRepository.count();

        Payment payment = paymentRepository.save(Payment.of(this.store, this.user, new BigDecimal(10000), PaymentStatusType.WAITING));

        long afterCount = paymentRepository.count();

        assertNotNull(payment.getPaymentId());
        assertEquals(beforeCount + 1, afterCount);
    }
}