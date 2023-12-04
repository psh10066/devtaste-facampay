package com.devtaste.facampay.application.payment.event;

import com.devtaste.facampay.domain.model.paymentAttempt.type.PaymentFailureType;

public record PaymentAttemptEvent(Long paymentId, PaymentFailureType paymentFailureType) {

    public static PaymentAttemptEvent of(Long paymentId, PaymentFailureType paymentFailureType) {
        return new PaymentAttemptEvent(paymentId, paymentFailureType);
    }
}
