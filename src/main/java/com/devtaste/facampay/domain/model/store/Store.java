package com.devtaste.facampay.domain.model.store;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private Store(String storeEmail, String storeName, Long money) {
        this.storeEmail = storeEmail;
        this.storeName = storeName;
        this.money = money;
    }

    public static Store of(String storeEmail, String storeName, Long money) {
        return new Store(storeEmail, storeName, money);
    }

    private Store(Long storeId, String storeEmail, String storeName, Long money) {
        this.storeId = storeId;
        this.storeEmail = storeEmail;
        this.storeName = storeName;
        this.money = money;
    }

    public static Store of(Long storeId, String storeEmail, String storeName, Long money) {
        return new Store(storeId, storeEmail, storeName, money);
    }

    public void changeMoney(long money) {
        this.money += money;
    }

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
