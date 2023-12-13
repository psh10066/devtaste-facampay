package com.devtaste.facampay.domain.model.payment;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import com.devtaste.facampay.domain.model.payment.type.PaymentFailureType;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.Objects;

@Comment("결제")
@Entity
@ToString(callSuper = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AuditingFields {

    @Comment("결제 고유번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Comment("가맹점 고유번호")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "payment_store_fk"), updatable = false)
    private Store store;

    @Comment("사용자 고유번호")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "payment_user_fk"), updatable = false)
    private User user;

    @Comment("결제 금액")
    @Column(nullable = false, scale = 4, precision = 19)
    private BigDecimal money;

    @Comment("결제 상태")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus;

    @Comment("결제 실패 사유")
    @Enumerated(EnumType.STRING)
    private PaymentFailureType paymentFailureType;

    private Payment(Store store, User user, BigDecimal money, PaymentStatusType paymentStatus) {
        this.store = store;
        this.user = user;
        this.money = money;
        this.paymentStatus = paymentStatus;
    }

    public static Payment of(Store store, User user, BigDecimal money, PaymentStatusType paymentStatus) {
        return new Payment(store, user, money, paymentStatus);
    }

    private Payment(Long paymentId, Store store, User user, BigDecimal money, PaymentStatusType paymentStatus) {
        this.paymentId = paymentId;
        this.store = store;
        this.user = user;
        this.money = money;
        this.paymentStatus = paymentStatus;
    }

    public static Payment of(Long paymentId, Store store, User user, BigDecimal money, PaymentStatusType paymentStatus) {
        return new Payment(paymentId, store, user, money, paymentStatus);
    }

    public void doPaymentAttempt(PaymentFailureType paymentFailureType) {
        if (paymentFailureType == null) {
            this.paymentStatus = PaymentStatusType.SUCCESS;
        } else {
            this.paymentStatus = PaymentStatusType.FAILURE;
            this.paymentFailureType = paymentFailureType;
        }
    }

    public void cancelPayment() {
        this.paymentStatus = PaymentStatusType.CANCELED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment that)) return false;
        return this.getPaymentId() != null && this.getPaymentId().equals(that.getPaymentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPaymentId());
    }
}
