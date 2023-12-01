package com.devtaste.facampay.application.payment;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.service.PaymentService;
import com.devtaste.facampay.domain.service.StoreService;
import com.devtaste.facampay.domain.service.UserService;
import com.devtaste.facampay.infrastructure.exception.NotFoundDataException;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final StoreService storeService;
    private final UserService userService;
    private final PaymentService paymentService;

    /**
     * 결제 요청
     */
    public void postPayment(PostPaymentRequest request) {
        Store store = storeService.findById(request.getStoreId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 가맹점입니다."));
        User user = userService.findById(request.getUserId()).orElseThrow(() -> new NotFoundDataException("존재하지 않는 사용자입니다."));

        paymentService.save(Payment.builder()
            .store(store)
            .user(user)
            .money(request.getMoney())
            .paymentStatus(PaymentStatusType.WAITING)
            .build());
    }
}
