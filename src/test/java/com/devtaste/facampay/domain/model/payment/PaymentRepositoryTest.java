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
        this.storeRepository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();
        this.store = this.storeRepository.save(Store.of("store@facam.com", "가맹점1", 0L));
        this.user = this.userRepository.save(User.of("user@facam.com", "사용자1", 25000L));
    }

    @DisplayName("결제 저장")
    @Test
    void save() {
        long beforeCount = paymentRepository.count();

        Payment payment = paymentRepository.save(Payment.of(this.store, this.user, 10000L, PaymentStatusType.WAITING));

        long afterCount = paymentRepository.count();

        assertNotNull(payment.getPaymentId());
        assertEquals(beforeCount + 1, afterCount);
    }
}