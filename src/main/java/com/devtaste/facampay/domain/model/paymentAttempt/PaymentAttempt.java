package com.devtaste.facampay.domain.model.paymentAttempt;

import com.devtaste.facampay.domain.model.common.DateColumn;
import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.paymentAttempt.type.PaymentFailureType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Comment("결제 시도")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentAttempt extends DateColumn {

    @Comment("결제 시도 고유번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentAttemptId;

    @Comment("결제 고유번호")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paymentId", foreignKey = @ForeignKey(name = "payment_attempt_payment_fk"), updatable = false)
    private Payment payment;

    @Comment("결제 성공 여부")
    @Column(nullable = false)
    private Boolean paymentStatus;

    @Comment("결제 실패 사유")
    @Enumerated(EnumType.STRING)
    private PaymentFailureType paymentFailureType;
}
