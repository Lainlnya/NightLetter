package com.nightletter.domain.diary.service;

import com.nightletter.domain.diary.dto.*;
import com.nightletter.global.common.ResponseDto;

import java.util.Optional;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryCreateResponse;
import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;

public interface DiaryService {

	Optional<DiaryCreateResponse> createDiary(DiaryCreateRequest diaryCreateRequest);

	Optional<DiaryResponse> updateDiaryDisclosure(DiaryDisclosureRequest request);

	Optional<DiaryListResponse> findDiaries(DiaryListRequest diaryListRequest);

	Optional<DiaryResponse> findDiary(Long diaryId);

	Optional<ResponseDto> deleteDiary(Long diaryId);
}
