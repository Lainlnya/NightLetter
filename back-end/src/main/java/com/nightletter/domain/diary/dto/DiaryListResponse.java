package com.nightletter.domain.diary.dto;

import java.util.List;

import lombok.Data;

@Data
public class DiaryListResponse {
	Integer requestDiaryIdx;
	private List<DiaryResponse> diaries;
}
