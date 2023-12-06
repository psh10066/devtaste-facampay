package com.devtaste.facampay.presentation.user.response;

import com.devtaste.facampay.application.payment.dto.PaymentDTO;
import com.devtaste.facampay.application.user.dto.UserDTO;

import java.util.List;

public record UserPaymentListResponse(UserDTO user, List<PaymentDTO> paymentList) {

    public static UserPaymentListResponse of(UserDTO user, List<PaymentDTO> paymentList) {
        return new UserPaymentListResponse(user, paymentList);
    }
}
