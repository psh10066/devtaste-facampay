package com.devtaste.facampay.presentation.user.response;

import com.devtaste.facampay.application.payment.dto.PaymentDTO;

import java.util.List;

public record PaymentListResponse(List<PaymentDTO> paymentList) {

    public static PaymentListResponse of(List<PaymentDTO> paymentList) {
        return new PaymentListResponse(paymentList);
    }
}
