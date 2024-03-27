package com.nightletter.domain.diary.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiaryUpdateRequest {
	private Long diaryId;
	private String content;
	private DiaryType type;

	public Diary toEntity() {
		LocalDate today = LocalDate.now();

		if (LocalTime.now().isBefore(LocalTime.of(4, 0))) {
			today = today.minusDays(1);
		}

		return Diary.builder()
			.diaryId(diaryId)
			.content(this.content)
			.type(this.type)
			.build();
	}
}
