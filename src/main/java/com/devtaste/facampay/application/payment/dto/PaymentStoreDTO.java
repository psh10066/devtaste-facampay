package com.devtaste.facampay.application.payment.dto;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;

import java.time.LocalDateTime;

public record PaymentStoreDTO(Long paymentId, String storeName, Long money, PaymentStatusType paymentStatus, LocalDateTime createdAt) {

    public static PaymentStoreDTO from(Payment payment) {
        return new PaymentStoreDTO(payment.getPaymentId(), payment.getStore().getStoreName(), payment.getMoney(), payment.getPaymentStatus(), payment.getCreatedAt());
    }

    public static PaymentStoreDTO of(Long paymentId, String storeName, Long money, PaymentStatusType paymentStatus, LocalDateTime createdAt) {
        return new PaymentStoreDTO(paymentId, storeName, money, paymentStatus, createdAt);
    }
}
