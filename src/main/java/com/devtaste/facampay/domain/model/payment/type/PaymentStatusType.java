package com.devtaste.facampay.domain.model.payment.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatusType {

    WAITING("결제 대기"),
    SUCCESS("결제 성공"),
    FAILURE("결제 실패"),
    CANCELED("결제 취소");

    private final String description;
}
