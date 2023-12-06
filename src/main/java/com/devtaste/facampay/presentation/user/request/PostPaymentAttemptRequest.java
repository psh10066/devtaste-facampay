package com.devtaste.facampay.presentation.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostPaymentAttemptRequest {

    @NotNull(message = "사용자 정보가 누락되었습니다.")
    private Long userId;

    @NotNull(message = "결제 정보를 선택해 주세요.")
    private Long paymentId;
}
