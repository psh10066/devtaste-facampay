package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.application.payment.event.PaymentAttemptEvent;
import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.paymentAttempt.type.PaymentFailureType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.infrastructure.exception.BadRequestApiException;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import com.devtaste.facampay.presentation.user.PostPaymentAttemptRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

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
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

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

    @DisplayName("결제 시도 - 성공")
    @Test
    void postPaymentAttempt_success() {
        PostPaymentAttemptRequest request = new PostPaymentAttemptRequest(1L, 2L);
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", 0L));
        Optional<User> user = Optional.of(User.of(request.getUserId(), "user@facam.com", "사용자1", 25000L));

        given(paymentRepository.findById(request.getPaymentId())).willReturn(Optional.of(Payment.of(store.get(), user.get(), 10000L, PaymentStatusType.WAITING)));
        willDoNothing().given(applicationEventPublisher).publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), null));

        paymentService.postPaymentAttempt(request);

        assertEquals(store.get().getMoney(), 10000L);
        assertEquals(user.get().getMoney(), 15000L);
        then(paymentRepository).should().findById(request.getPaymentId());
        then(applicationEventPublisher).should().publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), null));
    }

    @DisplayName("결제 시도 - 금액 부족")
    @Test
    void postPaymentAttempt_SHORTAGE_OF_MONEY() {
        PostPaymentAttemptRequest request = new PostPaymentAttemptRequest(1L, 2L);
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", 0L));
        Optional<User> user = Optional.of(User.of(request.getUserId(), "user@facam.com", "사용자1", 5000L));

        given(paymentRepository.findById(request.getPaymentId())).willReturn(Optional.of(Payment.of(store.get(), user.get(), 10000L, PaymentStatusType.WAITING)));
        willDoNothing().given(applicationEventPublisher).publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), PaymentFailureType.SHORTAGE_OF_MONEY));

        assertThrows(BadRequestApiException.class, () -> paymentService.postPaymentAttempt(request));

        assertEquals(store.get().getMoney(), 0L);
        assertEquals(user.get().getMoney(), 5000L);
        then(paymentRepository).should().findById(request.getPaymentId());
        then(applicationEventPublisher).should().publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), PaymentFailureType.SHORTAGE_OF_MONEY));
    }
}