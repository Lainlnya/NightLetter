package com.nightletter.domain.diary.service;

import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryRequest;
import com.nightletter.domain.diary.dto.DiaryUpdateRequest;
import com.nightletter.domain.diary.entity.Diary;

public interface DiaryService {

	Diary createDiary(DiaryRequest diaryRequest);

	Diary updateDiary(DiaryUpdateRequest diaryUpdateRequest);

	DiaryListResponse findDiaries(int id, DiaryRequest diaryRequest);

}
