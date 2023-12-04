package com.devtaste.facampay.domain.model.paymentAttempt;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.paymentAttempt.type.PaymentFailureType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.Objects;

@Comment("결제 시도")
@Entity
@ToString(callSuper = true)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentAttempt extends AuditingFields {

    @Comment("결제 시도 고유번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentAttemptId;

    @Comment("결제 고유번호")
    @ManyToOne(optional = false)
    @JoinColumn(name = "paymentId", foreignKey = @ForeignKey(name = "payment_attempt_payment_fk"), updatable = false)
    private Payment payment;

    @Comment("결제 성공 여부")
    @Column(nullable = false)
    private Boolean paymentStatus;

    @Comment("결제 실패 사유")
    @Enumerated(EnumType.STRING)
    private PaymentFailureType paymentFailureType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentAttempt that)) return false;
        return this.getPaymentAttemptId() != null && this.getPaymentAttemptId().equals(that.getPaymentAttemptId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPaymentAttemptId());
    }
}
