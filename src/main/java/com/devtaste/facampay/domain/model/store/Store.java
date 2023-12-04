package com.devtaste.facampay.domain.model.store;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import com.devtaste.facampay.domain.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.Objects;

@Comment("가맹점")
@Entity
@ToString(callSuper = true)
@Table(
    name = "store",
    uniqueConstraints = {
        @UniqueConstraint(name = "store_email_uk", columnNames = {"storeEmail"})
    }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Store extends AuditingFields {

    @Comment("가맹점 고유번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Comment("가맹점 이메일")
    @Column(nullable = false)
    private String storeEmail;

    @Comment("가맹점 명")
    @Column(nullable = false)
    private String storeName;

    @Comment("잔고")
    @Column(nullable = false)
    private Long money;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store that)) return false;
        return this.getStoreId() != null && this.getStoreId().equals(that.getStoreId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getStoreId());
    }
}
