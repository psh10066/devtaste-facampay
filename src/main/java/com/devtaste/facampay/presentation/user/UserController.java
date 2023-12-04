package com.devtaste.facampay.presentation.user;

import com.devtaste.facampay.application.payment.PaymentService;
import com.devtaste.facampay.presentation.common.response.SuccessResponse;
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
@RequestMapping("/user")
public class UserController {

    private final PaymentService paymentService;

    @PostMapping(value = "/payment/attempt", name = "결제 시도")
    public SuccessResponse postPaymentAttempt(@Valid @RequestBody PostPaymentAttemptRequest request) {
        paymentService.postPaymentAttempt(request);
        return new SuccessResponse("결제가 정상적으로 완료되었습니다.");
    }
}
