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
	int memberId;
	LocalDate recommendedOn;
	boolean isScrapped;

	@QueryProjection
	public DiaryRecResponse(long diaryId, int memberId, LocalDate recommendedOn, boolean isScrapped) {
		this.diaryId = diaryId;
		this.memberId = memberId;
		this.recommendedOn = recommendedOn;
		this.isScrapped = isScrapped;
	}

}
