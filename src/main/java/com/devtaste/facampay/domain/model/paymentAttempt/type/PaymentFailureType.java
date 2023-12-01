package com.devtaste.facampay.domain.model.paymentAttempt.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentFailureType {

    SHORTAGE_OF_MONEY("금액 부족");

    private final String reason;
}
