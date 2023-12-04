package com.devtaste.facampay.domain.model.payment;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.paymentAttempt.PaymentAttempt;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.List;
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "payment_store_fk"), updatable = false)
    private Store store;

    @Comment("사용자 고유번호")
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "payment_user_fk"), updatable = false)
    private User user;

    @Comment("결제 금액")
    @Column(nullable = false)
    private Long money;

    @Comment("결제 상태")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus;

    @ToString.Exclude
    @OneToMany(mappedBy = "payment")
    private List<PaymentAttempt> paymentAttemptList;

    private Payment(Store store, User user, Long money, PaymentStatusType paymentStatus) {
        this.store = store;
        this.user = user;
        this.money = money;
        this.paymentStatus = paymentStatus;
    }

    public static Payment of(Store store, User user, Long money, PaymentStatusType paymentStatus) {
        return new Payment(store, user, money, paymentStatus);
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
