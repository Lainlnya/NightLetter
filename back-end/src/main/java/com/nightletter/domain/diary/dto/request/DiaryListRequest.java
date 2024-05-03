package com.nightletter.domain.diary.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DiaryListRequest {
	private LocalDate sttDate;
	private LocalDate endDate;
}
