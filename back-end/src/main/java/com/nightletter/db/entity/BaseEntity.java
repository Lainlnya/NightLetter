package com.nightletter.db.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @CreatedDate
    private LocalDateTime createdAt;
    @CreatedBy @OneToOne
    @JoinColumn(name = "created_by", referencedColumnName = "member_id", updatable = false)
    private Member createdBy;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @LastModifiedBy @OneToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "member_id")
    private Member updatedBy;
}
