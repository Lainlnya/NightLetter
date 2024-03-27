package com.nightletter.domain.diary.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class DiaryCreateRequest {
	private String content;
	private DiaryType type;

	public Diary toEntity() {
		LocalDate today = LocalDate.now();

		if (LocalTime.now().isBefore(LocalTime.of(4, 0))) {
			today = today.minusDays(1);
		}

		return Diary.builder()
			.content(this.content)
			.date(today)
			.type(this.type)
			.build();
	}
}
