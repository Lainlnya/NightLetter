package com.nightletter.domain.diary.service;

import java.util.Optional;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryDisclosureRequest;
import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.diary.dto.GPTResponse;
import com.nightletter.domain.diary.dto.recommend.RecommendResponse;
import com.nightletter.global.common.ResponseDto;

public interface DiaryService {

	RecommendResponse createDiary(DiaryCreateRequest diaryCreateRequest);

	Optional<DiaryResponse> updateDiaryDisclosure(DiaryDisclosureRequest request);

	Optional<DiaryListResponse> findDiaries(DiaryListRequest diaryListRequest);

	Optional<DiaryResponse> findDiary(Long diaryId);

	Optional<ResponseDto> deleteDiary(Long diaryId);
}
