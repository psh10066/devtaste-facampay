package com.devtaste.facampay.application.payment.dto;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record PaymentDTO(Long paymentId, String storeName, Long money, PaymentStatusType paymentStatus,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt) {

    public static PaymentDTO from(Payment payment) {
        return new PaymentDTO(payment.getPaymentId(), payment.getStore().getStoreName(), payment.getMoney(), payment.getPaymentStatus(), payment.getCreatedAt());
    }

    public static PaymentDTO of(Long paymentId, String storeName, Long money, PaymentStatusType paymentStatus, LocalDateTime createdAt) {
        return new PaymentDTO(paymentId, storeName, money, paymentStatus, createdAt);
    }
}
