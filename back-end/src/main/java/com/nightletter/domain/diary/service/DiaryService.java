package com.nightletter.domain.diary.service;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nightletter.domain.diary.dto.*;
import com.nightletter.global.common.ResponseDto;

public interface DiaryService {

	Optional<RecommendResponse> createDiary(DiaryCreateRequest diaryCreateRequest) throws JsonProcessingException;

	Optional<DiaryResponse> updateDiaryDisclosure(DiaryDisclosureRequest request);

	Optional<DiaryListResponse> findDiaries(DiaryListRequest diaryListRequest);

	Optional<DiaryResponse> findDiary(Long diaryId);

	Optional<ResponseDto> deleteDiary(Long diaryId);

	Optional<GPTResponse> findGptComment();
}
