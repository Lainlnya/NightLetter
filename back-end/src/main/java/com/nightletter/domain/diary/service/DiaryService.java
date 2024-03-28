package com.nightletter.domain.diary.service;

import com.nightletter.domain.diary.dto.*;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryType;
import com.nightletter.global.common.ResponseDto;

import java.util.Optional;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryCreateResponse;
import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;

public interface DiaryService {

	Optional<DiaryCreateResponse> createDiary(DiaryCreateRequest diaryCreateRequest);

	Optional<Diary> updateDiaryDisclosure(Integer diaryId, DiaryOpenType diaryOpenType);
	Optional<DiaryResponse> updateDiaryDisclosure(DiaryDisclosureRequest request);

	Optional<DiaryListResponse> findDiaries(DiaryListRequest diaryListRequest);

	Optional<DiaryResponse> findDiary(Long diaryId);

	Optional<ResponseDto> deleteDiary(Long diaryId);
}
