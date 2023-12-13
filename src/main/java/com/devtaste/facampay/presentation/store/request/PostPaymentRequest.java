package com.devtaste.facampay.presentation.store.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostPaymentRequest {

    @NotNull(message = "가맹점 정보가 누락되었습니다.")
    private Long storeId;

    @NotNull(message = "사용자를 선택해 주세요.")
    private Long userId;

    @NotNull(message = "금액을 입력해 주세요.")
    @Min(value = 1, message = "1원부터 결제할 수 있습니다.")
    private BigDecimal money;
}
