package com.devtaste.facampay.presentation.store;

import com.devtaste.facampay.application.payment.PaymentApplicationService;
import com.devtaste.facampay.presentation.common.response.SuccessResponse;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final PaymentApplicationService paymentApplicationService;

    @PostMapping(value = "/payment", name = "결제 요청")
    public SuccessResponse postPayment(@Valid @RequestBody PostPaymentRequest request) {
        paymentApplicationService.postPayment(request);
        return new SuccessResponse("결제가 정상적으로 요청되었습니다.");
    }
}
