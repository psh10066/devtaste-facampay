package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.application.payment.dto.PaymentStoreDTO;
import com.devtaste.facampay.application.payment.event.PaymentAttemptEvent;
import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentFailureType;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUser;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUserRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.infrastructure.exception.BadRequestApiException;
import com.devtaste.facampay.infrastructure.exception.NotFoundDataException;
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
    private final PaymentRepository paymentRepository;
    private final StoreToUserRepository storeToUserRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 결제 요청
     */
    public void postPayment(PostPaymentRequest request) {
        StoreToUser storeToUser = storeToUserRepository.findByStoreStoreIdAndUserUserId(request.getStoreId(), request.getUserId()).orElseThrow(() -> new NotFoundDataException("가입되지 않은 사용자입니다."));
        if (!paymentRepository.findByStore_StoreIdAndUser_UserIdAndPaymentStatus(request.getStoreId(), request.getUserId(), PaymentStatusType.WAITING).isEmpty()) {
            throw new BadRequestApiException("해당 사용자에게 대기중인 결제 요청이 존재합니다.");
        }

        paymentRepository.save(Payment.of(storeToUser.getStore(), storeToUser.getUser(), request.getMoney(), PaymentStatusType.WAITING));
    }

    /**
     * 결제 목록 조회
     */
    public List<PaymentStoreDTO> getPaymentList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundDataException("존재하지 않는 사용자입니다."));
        return paymentRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(PaymentStoreDTO::from)
            .toList();
    }

    /**
     * 결제 시도
     */
    public void postPaymentAttempt(PostPaymentAttemptRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 결제 정보입니다."));
        User user = payment.getUser();

        if (!user.getUserId().equals(request.getUserId())) throw new BadRequestApiException("잘못된 요청입니다.");

        // 결제 실패
        if (user.getMoney() < payment.getMoney()) {
            applicationEventPublisher.publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), PaymentFailureType.SHORTAGE_OF_MONEY));
            throw new BadRequestApiException("잔액이 부족합니다.");
        }

        // 결제 성공
        applicationEventPublisher.publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), null));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void doPaymentAttemptEvent(PaymentAttemptEvent event) {
        Payment payment = paymentRepository.findByPaymentId(event.paymentId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 결제 정보입니다."));
        if (!payment.getPaymentStatus().isPayable()) throw new BadRequestApiException("종료된 결제 요청입니다.");

        payment.doPaymentAttempt(event.paymentFailureType());

        if (event.paymentFailureType() == null) {
            payment.getStore().changeMoney(payment.getMoney());
            payment.getUser().changeMoney(-payment.getMoney());
        }
    }

    /**
     * 결제 취소
     */
    public void cancelPayment(Long storeId, Long paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new NotFoundDataException("존재하지 않는 결제 정보입니다."));
        if (!payment.getStore().getStoreId().equals(storeId)) throw new BadRequestApiException("잘못된 요청입니다.");

        if (!payment.getPaymentStatus().equals(PaymentStatusType.WAITING)) throw new BadRequestApiException("대기중인 결제만 취소할 수 있습니다.");

        payment.cancelPayment();
    }
}
