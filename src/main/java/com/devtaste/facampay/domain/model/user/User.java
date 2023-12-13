package com.devtaste.facampay.domain.model.user;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.Objects;

@Comment("사용자")
@Entity
@ToString(callSuper = true)
@Table(
    name = "user",
    uniqueConstraints = {
        @UniqueConstraint(name = "user_email_uk", columnNames = {"userEmail"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingFields {

    @Comment("사용자 고유번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Comment("사용자 이메일")
    @Column(nullable = false)
    private String userEmail;

    @Comment("사용자 명")
    @Column(nullable = false)
    private String userName;

    @Comment("잔고")
    @Column(nullable = false)
    private BigDecimal money;

    private User(String userEmail, String userName, BigDecimal money) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.money = money;
    }

    public static User of(String userEmail, String userName, BigDecimal money) {
        return new User(userEmail, userName, money);
    }

    private User(Long userId, String userEmail, String userName, BigDecimal money) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.money = money;
    }

    public static User of(Long userId, String userEmail, String userName, BigDecimal money) {
        return new User(userId, userEmail, userName, money);
    }

    public void changeMoney(BigDecimal money) {
        this.money = this.money.add(money);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}
