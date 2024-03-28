package com.nightletter.domain.diary.dto;

import java.time.LocalDate;

import com.nightletter.domain.tarot.entity.Tarot;

import lombok.Data;

@Data
public class DiaryListRequest {
	private LocalDate date;
	private DiaryRequestDirection direction;
	private Integer size;
}
