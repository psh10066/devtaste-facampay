package com.devtaste.facampay.domain.model.user;

import com.devtaste.facampay.domain.model.common.AuditingFields;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

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
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private Long money;

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
