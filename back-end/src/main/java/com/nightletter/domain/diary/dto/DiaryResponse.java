package com.nightletter.domain.diary.dto;

import java.time.LocalDate;

import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.tarot.entity.Tarot;

import lombok.Builder;
import lombok.Data;

@Data
public class DiaryResponse {

	private Integer writerId;
	private Long diaryId;
	private DiaryOpenType type;
	private String content;
	private String gptComment;
	private Tarot pastCard;
	private Tarot nowCard;
	private Tarot futureCard;
	private LocalDate date;

	@Builder
	public DiaryResponse(Integer writerId, Long diaryId, String content, DiaryType type, String gptComment,
								Tarot pastCard, Tarot nowCard, Tarot futureCard, LocalDate date) {
		this.writerId = writerId;
		this.diaryId = diaryId;
		this.type = type;
		this.content = content;
		this.gptComment = gptComment;
		this.pastCard = pastCard;
		this.nowCard = nowCard;
		this.futureCard = futureCard;
		this.date = date;
	}
}
