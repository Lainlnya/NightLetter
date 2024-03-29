package com.nightletter.domain.diary.entity;

import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.entity.TarotDirection;
import com.nightletter.global.common.BaseTimeEntity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
	private DiaryTarotType type;
}
