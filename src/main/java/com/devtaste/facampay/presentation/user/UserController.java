package com.devtaste.facampay.presentation.user;

import com.devtaste.facampay.application.payment.PaymentService;
import com.devtaste.facampay.application.payment.dto.PaymentDTO;
import com.devtaste.facampay.presentation.common.response.DataResponse;
import com.devtaste.facampay.presentation.common.response.SuccessResponse;
import com.devtaste.facampay.presentation.user.response.PaymentListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final PaymentService paymentService;

    @GetMapping(value = "/payment/list/{userId}", name = "결제 목록 조회")
    public SuccessResponse getPaymentList(@PathVariable Long userId) {
        List<PaymentDTO> paymentList = paymentService.getPaymentList(userId);
        return new DataResponse<>(PaymentListResponse.of(paymentList));
    }

    @PostMapping(value = "/payment/attempt", name = "결제 시도")
    public SuccessResponse postPaymentAttempt(@Valid @RequestBody PostPaymentAttemptRequest request) {
        paymentService.postPaymentAttempt(request);
        return new SuccessResponse("결제가 정상적으로 완료되었습니다.");
    }
}
