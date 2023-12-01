package com.devtaste.facampay.domain.model.user;

import com.devtaste.facampay.domain.model.common.DateColumn;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Comment("사용자")
@Entity
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
public class User extends DateColumn {

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
}
