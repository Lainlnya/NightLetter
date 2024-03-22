package com.nightletter.db.entity;

import com.nightletter.db.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "diary_id"))
public class Diary extends BaseEntity{
    private Integer writerId;
    private LocalDate date;
    private String content;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String gptComment;
}

