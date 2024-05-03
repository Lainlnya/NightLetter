package com.nightletter.domain.diary.service;

import java.util.List;
import java.util.Optional;

import com.nightletter.domain.diary.dto.request.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.request.DiaryDisclosureRequest;
import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.response.DiaryResponse;
import com.nightletter.domain.diary.dto.recommend.RecommendResponse;
import com.nightletter.global.common.ResponseDto;

public interface DiaryService {

	RecommendResponse createDiary(DiaryCreateRequest diaryCreateRequest);

	Optional<DiaryResponse> updateDiaryDisclosure(DiaryDisclosureRequest request);

	List<DiaryResponse> findDiaries(DiaryListRequest diaryListRequest);

	Optional<DiaryResponse> findDiary(Long diaryId);

	Optional<ResponseDto> deleteDiary(Long diaryId);

	Optional<String> createDiaryShareUrl(Long diaryId);
}
