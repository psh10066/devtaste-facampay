package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @DisplayName("결제 요청")
    @Test
    void postPayment() {
        PostPaymentRequest request = new PostPaymentRequest(1L, 2L, 10000L);
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", 0L));
        Optional<User> user = Optional.of(User.of("user@facam.com", "사용자1", 25000L));
        given(storeRepository.findById(request.getStoreId())).willReturn(store);
        given(userRepository.findById(request.getUserId())).willReturn(user);
        given(paymentRepository.save(any(Payment.class))).willReturn(Payment.of(store.get(), user.get(), request.getMoney(), PaymentStatusType.WAITING));

        paymentService.postPayment(request);

        then(storeRepository).should().findById(request.getStoreId());
        then(userRepository).should().findById(request.getUserId());
        then(paymentRepository).should().save(any(Payment.class));
    }
}