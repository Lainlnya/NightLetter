package com.nightletter.domain.diary.service;

import org.springframework.stereotype.Service;

import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryRequest;
import com.nightletter.domain.diary.dto.DiaryUpdateRequest;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;

	@Override
	public Diary createDiary(DiaryRequest diaryRequest) {
		return diaryRepository.save(diaryRequest.toEntity());
	}

	@Override
	public Diary updateDiary(DiaryUpdateRequest diaryUpdateRequest) {
		return diaryRepository.save(diaryUpdateRequest.toEntity());
	}

	@Override
	public DiaryListResponse findDiaries(int id, DiaryRequest diaryRequest) {
		return null;
	}
}
