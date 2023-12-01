package com.devtaste.facampay.domain.model.payment;

import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.common.DateColumn;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.paymentAttempt.PaymentAttempt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@Comment("결제")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment extends DateColumn {

    @Comment("결제 고유번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Comment("가맹점 고유번호")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "payment_store_fk"), updatable = false)
    private Store store;

    @Comment("사용자 고유번호")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "payment_user_fk"), updatable = false)
    private User user;

    @Comment("결제 금액")
    @Column(nullable = false)
    private Long money;

    @Comment("결제 상태")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus;

    @OneToMany(mappedBy = "payment")
    private List<PaymentAttempt> paymentAttemptList;
}
