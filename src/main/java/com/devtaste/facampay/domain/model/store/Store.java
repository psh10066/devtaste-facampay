package com.devtaste.facampay.domain.model.store;

import com.devtaste.facampay.domain.model.common.DateColumn;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Comment("가맹점")
@Entity
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
public class Store extends DateColumn {

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
}
