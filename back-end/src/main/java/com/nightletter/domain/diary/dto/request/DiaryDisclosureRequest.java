package com.nightletter.domain.diary.dto.request;

import com.nightletter.domain.diary.entity.DiaryOpenType;

import lombok.Data;

@Data
public class DiaryDisclosureRequest {

	Long diaryId;
	DiaryOpenType type;
}
