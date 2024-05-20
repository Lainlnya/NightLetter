package com.nightletter.domain.diary.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder @ToString
public class DiaryCreateEvent {
	Long diaryId;
	Integer memberId;
	List<Long> recommendedDiaryIdList;
}
