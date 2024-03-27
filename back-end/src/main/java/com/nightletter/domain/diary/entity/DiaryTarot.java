package com.nightletter.domain.diary.entity;

import com.nightletter.global.common.BaseEntity;
import com.nightletter.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
public class DiaryTarot extends BaseTimeEntity {

    @EmbeddedId
    private DiaryTarotId id;

    @MapsId("diaryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @MapsId("tarotId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarot_id")
    private Tarot tarot;

    @Enumerated(EnumType.STRING)
    private TarotDirection direction;

    @Enumerated(EnumType.STRING)
    private TarotType type;
}
