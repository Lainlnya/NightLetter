package com.nightletter.domain.diary.dto.response;

import java.time.LocalDate;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.dto.TarotDto;

import lombok.Builder;
import lombok.Data;

@Data
public class DiaryResponse {

	private Integer writerId;
	private Long diaryId;
	private DiaryOpenType type;
	private String content;
	private String gptComment;
	private TarotDto pastCard;
	private TarotDto nowCard;
	private TarotDto futureCard;
	private LocalDate date;

	@Builder
	public DiaryResponse(Integer writerId, Long diaryId, String content, DiaryOpenType type, String gptComment,
		TarotDto pastCard, TarotDto nowCard, TarotDto futureCard, LocalDate date) {
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
		TarotDto past = null;
		TarotDto curr = null;
		TarotDto future = null;

		for (DiaryTarot diaryTarot : diary.getDiaryTarots()) {
			if (diaryTarot.getTarot() == null)
				continue;

			TarotDto tarot = TarotDto.of(diaryTarot.getTarot(), diaryTarot.getTarot().getDir());

			if (diaryTarot.getType() == DiaryTarotType.PAST) {
				past = tarot;
			}
			if (diaryTarot.getType() == DiaryTarotType.NOW) {
				curr = tarot;
			}
			if (diaryTarot.getType() == DiaryTarotType.FUTURE) {
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
