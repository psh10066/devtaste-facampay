package com.devtaste.facampay.domain.model.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class DateColumn {

    @Comment("생성 시각")
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Comment("수정 시각")
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
