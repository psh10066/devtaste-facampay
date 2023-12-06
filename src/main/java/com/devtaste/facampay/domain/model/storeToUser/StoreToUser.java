package com.devtaste.facampay.domain.model.storeToUser;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.Objects;

@Comment("가맹점 회원")
@Entity
@ToString(callSuper = true)
@Table(
    name = "store_to_user",
    uniqueConstraints = {
        @UniqueConstraint(name = "store_user_uk", columnNames = {"storeId", "userId"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreToUser extends AuditingFields {

    @Comment("가맹점 회원 고유번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeToUserId;

    @Comment("가맹점 고유번호")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "store_to_user_store_fk"), updatable = false)
    private Store store;

    @Comment("사용자 고유번호")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "store_to_user_user_fk"), updatable = false)
    private User user;

    private StoreToUser(Store store, User user) {
        this.store = store;
        this.user = user;
    }

    public static StoreToUser of(Store store, User user) {
        return new StoreToUser(store, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreToUser that)) return false;
        return this.getStoreToUserId() != null && this.getStoreToUserId().equals(that.getStoreToUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getStoreToUserId());
    }
}
