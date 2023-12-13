package com.devtaste.facampay.application.payment.dto;

import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(Long paymentId, BigDecimal money, PaymentStatusType paymentStatus, LocalDateTime createdAt) {

    public static PaymentDTO from(Payment payment) {
        return new PaymentDTO(payment.getPaymentId(), payment.getMoney(), payment.getPaymentStatus(), payment.getCreatedAt());
    }

    public static PaymentDTO of(Long paymentId, BigDecimal money, PaymentStatusType paymentStatus, LocalDateTime createdAt) {
        return new PaymentDTO(paymentId, money, paymentStatus, createdAt);
    }
}
