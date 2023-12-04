package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.store.StoreRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.infrastructure.exception.NotFoundDataException;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 결제 요청
     */
    public void postPayment(PostPaymentRequest request) {
        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 가맹점입니다."));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 사용자입니다."));

        paymentRepository.save(Payment.of(store, user, request.getMoney(), PaymentStatusType.WAITING));
    }
}
