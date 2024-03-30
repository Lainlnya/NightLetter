package com.nightletter.domain.diary.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.member.entity.Member;

import lombok.Data;

@Data
public class DiaryCreateRequest {
	private String content;
	private DiaryOpenType type;

	public Diary toEntity(Member writer, EmbedVector embedVector) {
		LocalDate today = LocalDate.now();

		if (LocalTime.now().isBefore(LocalTime.of(4, 0))) {
			today = today.minusDays(1);
		}

		return Diary.builder()
			.content(this.content)
			.writer(writer)
			.date(today)
			.type(this.type)
			.vector(embedVector.convertString())
			.build();
	}
}
