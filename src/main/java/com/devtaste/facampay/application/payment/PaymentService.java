package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.application.payment.dto.PaymentStoreDTO;
import com.devtaste.facampay.application.payment.event.PaymentAttemptEvent;
import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentFailureType;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUser;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUserRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.infrastructure.exception.CustomException;
import com.devtaste.facampay.infrastructure.exception.response.type.ErrorType;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import com.devtaste.facampay.presentation.user.request.PostPaymentAttemptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PaymentRepository paymentRepository;
    private final StoreToUserRepository storeToUserRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 결제 요청
     */
    public void postPayment(PostPaymentRequest request) {
        StoreToUser storeToUser = storeToUserRepository.findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId()).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_STORE_TO_USER));
        if (!paymentRepository.findByStore_StoreIdAndUser_UserIdAndPaymentStatus(request.getStoreId(), request.getUserId(), PaymentStatusType.WAITING).isEmpty()) {
            throw new CustomException(ErrorType.EXIST_WAITING_PAYMENT);
        }

        paymentRepository.save(Payment.of(storeToUser.getStore(), storeToUser.getUser(), request.getMoney(), PaymentStatusType.WAITING));
    }

    /**
     * 결제 목록 조회
     */
    public List<PaymentStoreDTO> getPaymentList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
        return paymentRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(PaymentStoreDTO::from)
            .toList();
    }

    /**
     * 결제 시도
     */
    public void postPaymentAttempt(PostPaymentAttemptRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId()).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PAYMENT));
        User user = payment.getUser();

        if (!user.getUserId().equals(request.getUserId())) throw new CustomException(ErrorType.BAD_REQUEST);

        // 결제 실패
        if (user.getMoney() < payment.getMoney()) {
            applicationEventPublisher.publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), PaymentFailureType.SHORTAGE_OF_MONEY));
            throw new CustomException(ErrorType.SHORTAGE_OF_MONEY);
        }

        // 결제 성공
        applicationEventPublisher.publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), null));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void doPaymentAttemptEvent(PaymentAttemptEvent event) {
        Payment payment = paymentRepository.findByPaymentId(event.paymentId()).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PAYMENT));
        if (!payment.getPaymentStatus().isPayable()) throw new CustomException(ErrorType.FINISHED_PAYMENT);

        payment.doPaymentAttempt(event.paymentFailureType());

        if (event.paymentFailureType() == null) {
            storeRepository.findByStoreId(payment.getStore().getStoreId()).get().changeMoney(payment.getMoney());
            userRepository.findByUserId(payment.getUser().getUserId()).get().changeMoney(-payment.getMoney());
        }
    }

    /**
     * 결제 취소
     */
    public void cancelPayment(Long storeId, Long paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PAYMENT));
        if (!payment.getStore().getStoreId().equals(storeId)) throw new CustomException(ErrorType.BAD_REQUEST);

        if (!payment.getPaymentStatus().equals(PaymentStatusType.WAITING))
            throw new CustomException(ErrorType.NOT_CANCELABLE_PAYMENT);

        payment.cancelPayment();
    }
}
