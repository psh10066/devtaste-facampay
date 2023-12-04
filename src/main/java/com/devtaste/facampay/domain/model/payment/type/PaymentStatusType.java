package com.devtaste.facampay.domain.model.payment.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatusType {

    WAITING("결제 대기", true),
    SUCCESS("결제 성공", false),
    FAILURE("결제 실패", true),
    CANCELED("결제 취소", false);

    private final String description;
    private final boolean payable;
}
