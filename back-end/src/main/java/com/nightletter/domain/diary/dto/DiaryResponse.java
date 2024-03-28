package com.nightletter.domain.diary.dto;

import java.time.LocalDate;

import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.tarot.entity.Tarot;

import lombok.Builder;
import lombok.Data;

import com.nightletter.domain.diary.entity.*;

import com.nightletter.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryResponse {

	private Integer writerId;
	private Long diaryId;
	private DiaryOpenType type;
	private String content;
	private String gptComment;
	private TarotResponse pastCard;
	private TarotResponse nowCard;
	private TarotResponse futureCard;
	private LocalDate date;

	@Builder
	public DiaryResponse(Integer writerId, Long diaryId, String content, DiaryType type, String gptComment,
						 TarotResponse pastCard, TarotResponse nowCard, TarotResponse futureCard, LocalDate date) {
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

	public static DiaryResponse of(Diary diary) {
		TarotResponse past = null;
		TarotResponse curr = null;
		TarotResponse future = null;

		for (DiaryTarot diaryTarot : diary.getDiaryTarots()) {
			TarotResponse tarot = TarotResponse.of(diaryTarot.getTarot(), diaryTarot.getDirection());

			if (diaryTarot.getType() == TarotType.PAST) {
				past = tarot;
			}
			if (diaryTarot.getType() == TarotType.NOW) {
				curr = tarot;
			}
			if (diaryTarot.getType() == TarotType.FUTURE) {
				future = tarot;
			}
			System.out.println(tarot);
		}

		return DiaryResponse.builder()
				.writerId(diary.getWriter().getMemberId())
				.diaryId(diary.getDiaryId())
				.type(diary.getType())
				.content(diary.getContent())
				.gptComment(diary.getGptComment())
				.pastCard(past)
				.nowCard(curr)
				.futureCard(future)
				.date(diary.getDate())
				.build();
	}
}
