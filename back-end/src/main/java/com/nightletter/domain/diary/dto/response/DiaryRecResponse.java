package com.nightletter.domain.diary.dto.response;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DiaryRecResponse {
	long diaryId;
	String nickname;
	String content;
	String imgUrl;
	Boolean isScrapped;

	@QueryProjection
	public DiaryRecResponse(long diaryId, String nickname, String content, String imgUrl, Boolean isScrapped) {
		this.diaryId = diaryId;
		this.nickname = nickname;
		this.content = content;
		this.imgUrl = imgUrl;
		this.isScrapped = isScrapped;
	}

}
