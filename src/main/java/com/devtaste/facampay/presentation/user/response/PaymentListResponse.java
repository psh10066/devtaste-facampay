package com.devtaste.facampay.presentation.user.response;

import com.devtaste.facampay.application.payment.dto.PaymentStoreDTO;

import java.util.List;

public record PaymentListResponse(List<PaymentStoreDTO> paymentList) {

    public static PaymentListResponse of(List<PaymentStoreDTO> paymentList) {
        return new PaymentListResponse(paymentList);
    }
}
