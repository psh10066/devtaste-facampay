package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.application.payment.dto.PaymentStoreDTO;
import com.devtaste.facampay.application.payment.event.PaymentAttemptEvent;
import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentFailureType;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUser;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUserRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.infrastructure.exception.CustomException;
import com.devtaste.facampay.infrastructure.exception.response.type.ErrorType;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import com.devtaste.facampay.presentation.user.request.PostPaymentAttemptRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
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
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private StoreToUserRepository storeToUserRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @DisplayName("결제 요청 - 성공")
    @Test
    void postPayment_success() {
        PostPaymentRequest request = new PostPaymentRequest(1L, 2L, new BigDecimal(10000));
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", new BigDecimal(0)));
        Optional<User> user = Optional.of(User.of("user@facam.com", "사용자1", new BigDecimal(25000)));

        given(storeToUserRepository.findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId())).willReturn(Optional.of(StoreToUser.of(store.get(), user.get())));
        given(paymentRepository.findByStore_StoreIdAndUser_UserIdAndPaymentStatus(request.getStoreId(), request.getUserId(), PaymentStatusType.WAITING)).willReturn(List.of());
        given(paymentRepository.save(any(Payment.class))).willReturn(Payment.of(store.get(), user.get(), request.getMoney(), PaymentStatusType.WAITING));

        paymentService.postPayment(request);

        then(storeToUserRepository).should().findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId());
        then(paymentRepository).should().findByStore_StoreIdAndUser_UserIdAndPaymentStatus(request.getStoreId(), request.getUserId(), PaymentStatusType.WAITING);
        then(paymentRepository).should().save(any(Payment.class));
    }

    @DisplayName("결제 요청 - 등록된 회원에 한해 결제 요청을 보낼 수 있음")
    @Test
    void postPayment_onlyRegisteredUser() {
        PostPaymentRequest request = new PostPaymentRequest(1L, 2L, new BigDecimal(10000));

        given(storeToUserRepository.findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId())).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> paymentService.postPayment(request));

        assertEquals(exception.getErrorType(), ErrorType.NOT_FOUND_STORE_TO_USER);

        then(storeToUserRepository).should().findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId());
        then(paymentRepository).should(never()).findByStore_StoreIdAndUser_UserIdAndPaymentStatus(anyLong(), anyLong(), any(PaymentStatusType.class));
        then(paymentRepository).should(never()).save(any(Payment.class));
    }

    @DisplayName("결제 요청 - 대기 건은 하나만 존재할 수 있음")
    @Test
    void postPayment_onlyOnePaymentWaiting() {
        PostPaymentRequest request = new PostPaymentRequest(1L, 2L, new BigDecimal(10000));
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", new BigDecimal(0)));
        Optional<User> user = Optional.of(User.of("user@facam.com", "사용자1", new BigDecimal(25000)));

        given(storeToUserRepository.findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId())).willReturn(Optional.of(StoreToUser.of(store.get(), user.get())));
        given(paymentRepository.findByStore_StoreIdAndUser_UserIdAndPaymentStatus(request.getStoreId(), request.getUserId(), PaymentStatusType.WAITING)).willReturn(List.of(Payment.of(store.get(), user.get(), request.getMoney(), PaymentStatusType.WAITING)));

        CustomException exception = assertThrows(CustomException.class, () -> paymentService.postPayment(request));

        assertEquals(exception.getErrorType(), ErrorType.EXIST_WAITING_PAYMENT);

        then(storeToUserRepository).should().findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId());
        then(paymentRepository).should().findByStore_StoreIdAndUser_UserIdAndPaymentStatus(request.getStoreId(), request.getUserId(), PaymentStatusType.WAITING);
        then(paymentRepository).should(never()).save(any(Payment.class));
    }

    @DisplayName("결제 목록 조회")
    @Test
    void getPaymentList() {
        long userId = 1L;
        Optional<User> user = Optional.of(User.of(userId, "user@facam.com", "사용자1", new BigDecimal(25000)));
        given(userRepository.findById(userId)).willReturn(user);
        given(paymentRepository.findByUserOrderByCreatedAtDesc(user.get())).willReturn(List.of(
            Payment.of(1L, Store.of("store1@facam.com", "가맹점1", new BigDecimal(0)), user.get(), new BigDecimal(10000), PaymentStatusType.WAITING),
            Payment.of(2L, Store.of("store2@facam.com", "가맹점2", new BigDecimal(0)), user.get(), new BigDecimal(5000), PaymentStatusType.SUCCESS)
        ));

        List<PaymentStoreDTO> paymentList = paymentService.getPaymentList(userId);

        assertEquals(paymentList.get(0).paymentId(), 1L);
        assertEquals(paymentList.get(0).storeName(), "가맹점1");
        assertEquals(paymentList.get(0).money(), new BigDecimal(10000));
        assertEquals(paymentList.get(0).paymentStatus(), PaymentStatusType.WAITING);
        assertEquals(paymentList.get(1).paymentId(), 2L);
        assertEquals(paymentList.get(1).storeName(), "가맹점2");
        assertEquals(paymentList.get(1).money(), new BigDecimal(5000));
        assertEquals(paymentList.get(1).paymentStatus(), PaymentStatusType.SUCCESS);
        then(userRepository).should().findById(userId);
        then(paymentRepository).should().findByUserOrderByCreatedAtDesc(user.get());
    }

    @DisplayName("결제 시도 - 성공")
    @Test
    void postPaymentAttempt_success() {
        PostPaymentAttemptRequest request = new PostPaymentAttemptRequest(1L, 2L);
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", new BigDecimal(0)));
        Optional<User> user = Optional.of(User.of(request.getUserId(), "user@facam.com", "사용자1", new BigDecimal(25000L)));
        Optional<Payment> payment = Optional.of(Payment.of(store.get(), user.get(), new BigDecimal(10000), PaymentStatusType.WAITING));
        PaymentAttemptEvent event = PaymentAttemptEvent.of(request.getPaymentId(), null);

        given(paymentRepository.findById(request.getPaymentId())).willReturn(payment);
        willDoNothing().given(applicationEventPublisher).publishEvent(event);
        given(paymentRepository.findByPaymentId(request.getPaymentId())).willReturn(payment);
        given(storeRepository.findByStoreId(payment.get().getStore().getStoreId())).willReturn(store);
        given(userRepository.findByUserId(payment.get().getUser().getUserId())).willReturn(user);

        paymentService.postPaymentAttempt(request);
        paymentService.doPaymentAttemptEvent(event);

        assertEquals(store.get().getMoney(), new BigDecimal(10000));
        assertEquals(user.get().getMoney(), new BigDecimal(15000));
        then(paymentRepository).should().findById(request.getPaymentId());
        then(applicationEventPublisher).should().publishEvent(event);
        then(paymentRepository).should().findByPaymentId(request.getPaymentId());
        then(storeRepository).should().findByStoreId(payment.get().getStore().getStoreId());
        then(userRepository).should().findByUserId(payment.get().getUser().getUserId());
    }

    @DisplayName("결제 시도 - 금액 부족")
    @Test
    void postPaymentAttempt_SHORTAGE_OF_MONEY() {
        PostPaymentAttemptRequest request = new PostPaymentAttemptRequest(1L, 2L);
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", new BigDecimal(0)));
        Optional<User> user = Optional.of(User.of(request.getUserId(), "user@facam.com", "사용자1", new BigDecimal(5000)));
        Optional<Payment> payment = Optional.of(Payment.of(store.get(), user.get(), new BigDecimal(10000), PaymentStatusType.WAITING));
        PaymentAttemptEvent event = PaymentAttemptEvent.of(request.getPaymentId(), PaymentFailureType.SHORTAGE_OF_MONEY);

        given(paymentRepository.findById(request.getPaymentId())).willReturn(payment);
        willDoNothing().given(applicationEventPublisher).publishEvent(event);
        given(paymentRepository.findByPaymentId(request.getPaymentId())).willReturn(payment);

        CustomException exception = assertThrows(CustomException.class, () -> paymentService.postPaymentAttempt(request));
        assertEquals(exception.getErrorType(), ErrorType.SHORTAGE_OF_MONEY);

        paymentService.doPaymentAttemptEvent(event);

        assertEquals(store.get().getMoney(), new BigDecimal(0));
        assertEquals(user.get().getMoney(), new BigDecimal(5000));
        then(paymentRepository).should().findById(request.getPaymentId());
        then(applicationEventPublisher).should().publishEvent(event);
        then(paymentRepository).should().findByPaymentId(request.getPaymentId());
        then(storeRepository).should(never()).findByStoreId(anyLong());
        then(userRepository).should(never()).findByUserId(anyLong());
    }

    @DisplayName("결제 시도 - 이미 완료된 결제")
    @Test
    void postPaymentAttempt_finished() {
        PostPaymentAttemptRequest request = new PostPaymentAttemptRequest(1L, 2L);
        Optional<Store> store = Optional.of(Store.of("store@facam.com", "가맹점1", new BigDecimal(0)));
        Optional<User> user = Optional.of(User.of(request.getUserId(), "user@facam.com", "사용자1", new BigDecimal(15000)));
        Optional<Payment> payment = Optional.of(Payment.of(store.get(), user.get(), new BigDecimal(10000), PaymentStatusType.SUCCESS));
        PaymentAttemptEvent event = PaymentAttemptEvent.of(request.getPaymentId(), null);

        given(paymentRepository.findById(request.getPaymentId())).willReturn(payment);
        willDoNothing().given(applicationEventPublisher).publishEvent(event);
        given(paymentRepository.findByPaymentId(request.getPaymentId())).willReturn(payment);

        paymentService.postPaymentAttempt(request);
        CustomException exception = assertThrows(CustomException.class, () -> paymentService.doPaymentAttemptEvent(event));
        assertEquals(exception.getErrorType(), ErrorType.FINISHED_PAYMENT);

        assertEquals(store.get().getMoney(), new BigDecimal(0));
        assertEquals(user.get().getMoney(), new BigDecimal(15000));
        then(paymentRepository).should().findById(request.getPaymentId());
        then(applicationEventPublisher).should().publishEvent(event);
        then(paymentRepository).should().findByPaymentId(request.getPaymentId());
        then(storeRepository).should(never()).findByStoreId(anyLong());
        then(userRepository).should(never()).findByUserId(anyLong());
    }

    @DisplayName("결제 취소 - 상태값에 따른 취소 가능 여부 확인")
    @Test
    void cancelPayment() {
        long storeId = 1;
        long paymentId = 2;

        Store store = Store.of(storeId, "store@facam.com", "가맹점1", new BigDecimal(0));
        User user = User.of("user@facam.com", "사용자1", new BigDecimal(5000));
        CustomException exception;

        given(paymentRepository.findByPaymentId(paymentId)).willReturn(Optional.of(Payment.of(store, user, new BigDecimal(10000), PaymentStatusType.WAITING)));
        paymentService.cancelPayment(storeId, paymentId);

        given(paymentRepository.findByPaymentId(paymentId)).willReturn(Optional.of(Payment.of(store, user, new BigDecimal(10000), PaymentStatusType.SUCCESS)));
        exception = assertThrows(CustomException.class, () -> paymentService.cancelPayment(storeId, paymentId));
        assertEquals(exception.getErrorType(), ErrorType.NOT_CANCELABLE_PAYMENT);

        given(paymentRepository.findByPaymentId(paymentId)).willReturn(Optional.of(Payment.of(store, user, new BigDecimal(10000), PaymentStatusType.FAILURE)));
        exception = assertThrows(CustomException.class, () -> paymentService.cancelPayment(storeId, paymentId));
        assertEquals(exception.getErrorType(), ErrorType.NOT_CANCELABLE_PAYMENT);

        given(paymentRepository.findByPaymentId(paymentId)).willReturn(Optional.of(Payment.of(store, user, new BigDecimal(10000), PaymentStatusType.CANCELED)));
        exception = assertThrows(CustomException.class, () -> paymentService.cancelPayment(storeId, paymentId));
        assertEquals(exception.getErrorType(), ErrorType.NOT_CANCELABLE_PAYMENT);

        then(paymentRepository).should(times(4)).findByPaymentId(paymentId);
    }
}