package com.nightletter.domain.diary.service;

import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryType;

import java.util.Optional;

public interface DiaryService {

	Optional<Diary> createDiary(DiaryCreateRequest diaryCreateRequest);

	Optional<Diary> updateDiaryDisclosure(Integer diaryId, DiaryType diaryType);

	Optional<DiaryListResponse> findDiaries(DiaryListRequest diaryListRequest);

}
