package com.nightletter.domain.diary.entity;

import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.global.common.BaseTimeEntity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@AllArgsConstructor
public class DiaryTarot extends BaseTimeEntity {

	@EmbeddedId
	private DiaryTarotId id;

	@MapsId("diaryId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary_id")
	@Setter
	private Diary diary;

	@MapsId("tarotId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tarot_id")
	private Tarot tarot;

	@Enumerated(EnumType.STRING)
	private DiaryTarotType type;

	public DiaryTarot() {

	}

	public DiaryTarot(Diary diary, Tarot tarot, DiaryTarotType type) {
		this.id = new DiaryTarotId(diary.getDiaryId(), tarot.getId());
		this.diary = diary;
		this.tarot = tarot;
		this.type = type;
	}
}
