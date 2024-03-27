package com.nightletter.domain.diary.dto;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryType;
import com.nightletter.domain.diary.entity.Tarot;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DiaryListRequest {
	private LocalDate date;
	private DiaryRequestDirection direction;
	private Integer size;
	private Tarot pastCard;
	private Tarot nowCard;
	private Tarot futureCard;
}
