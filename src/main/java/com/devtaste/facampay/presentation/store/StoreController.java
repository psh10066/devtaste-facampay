package com.devtaste.facampay.presentation.store;

import com.devtaste.facampay.application.payment.PaymentService;
import com.devtaste.facampay.application.user.UserService;
import com.devtaste.facampay.application.user.dto.UserDTO;
import com.devtaste.facampay.presentation.common.response.DataResponse;
import com.devtaste.facampay.presentation.common.response.SuccessResponse;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import com.devtaste.facampay.presentation.user.request.UserListRequest;
import com.devtaste.facampay.presentation.user.response.UserListResponse;
import com.devtaste.facampay.presentation.user.response.UserPaymentListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping(value = "/user/list/{storeId}", name = "회원 목록 조회")
    public SuccessResponse getUserList(@PathVariable Long storeId, UserListRequest request) {
        List<UserDTO> userList = userService.getUserList(storeId, request);
        return new DataResponse<>(UserListResponse.of(userList));
    }

    @GetMapping(value = "/user/{storeId}/{userId}", name = "회원 결제 정보 조회")
    public SuccessResponse getPaymentUser(@PathVariable Long storeId, @PathVariable Long userId) {
        UserPaymentListResponse response = userService.getPaymentUser(storeId, userId);
        return new DataResponse<>(response);
    }

    @PostMapping(value = "/payment", name = "결제 요청")
    public SuccessResponse postPayment(@Valid @RequestBody PostPaymentRequest request) {
        paymentService.postPayment(request);
        return new SuccessResponse("결제가 정상적으로 요청되었습니다.");
    }

    @DeleteMapping(value = "/payment/{storeId}/{paymentId}", name = "결제 취소")
    public SuccessResponse cancelPayment(@PathVariable Long storeId, @PathVariable Long paymentId) {
        paymentService.cancelPayment(storeId, paymentId);
        return new SuccessResponse("결제가 정상적으로 취소되었습니다.");
    }
}
