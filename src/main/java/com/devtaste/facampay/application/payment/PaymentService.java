package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.application.payment.dto.PaymentDTO;
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
import com.devtaste.facampay.infrastructure.exception.NotFoundDataException;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import com.devtaste.facampay.presentation.user.PostPaymentAttemptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 결제 요청
     */
    public void postPayment(PostPaymentRequest request) {
        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 가맹점입니다."));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 사용자입니다."));

        paymentRepository.save(Payment.of(store, user, request.getMoney(), PaymentStatusType.WAITING));
    }

    /**
     * 결제 목록 조회
     */
    public List<PaymentDTO> getPaymentList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundDataException("존재하지 않는 사용자입니다."));
        return paymentRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(PaymentDTO::from)
            .toList();
    }

    /**
     * 결제 시도
     */
    public void postPaymentAttempt(PostPaymentAttemptRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 결제 정보입니다."));
        User user = payment.getUser();

        if (!user.getUserId().equals(request.getUserId())) throw new BadRequestApiException("잘못된 요청입니다.");
        if (!payment.getPaymentStatus().isPayable()) throw new BadRequestApiException("종료된 결제 요청입니다.");

        // 결제 실패
        if (user.getMoney() < payment.getMoney()) {
            applicationEventPublisher.publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), PaymentFailureType.SHORTAGE_OF_MONEY));
            throw new BadRequestApiException("잔액이 부족합니다.");
        }

        // 결제 성공
        applicationEventPublisher.publishEvent(PaymentAttemptEvent.of(request.getPaymentId(), null));

        Store store = payment.getStore();
        store.changeMoney(payment.getMoney());
        user.changeMoney(-payment.getMoney());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void addPaymentAttemptEvent(PaymentAttemptEvent event) {
        Payment payment = paymentRepository.getReferenceById(event.paymentId());
        payment.addPaymentAttempt(event.paymentFailureType());
    }
}
