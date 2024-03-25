package com.nightletter.db.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nightletter.db.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AttributeOverride(name = "id", column = @Column(name = "diary_id"))
public class Diary extends BaseEntity {
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "member_id", updatable = false)
    private Member writer;

    private LocalDate date;

    private String content;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String gptComment;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "member_id")
    private Member updatedBy;
}

