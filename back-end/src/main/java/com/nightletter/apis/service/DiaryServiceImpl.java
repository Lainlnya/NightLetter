package com.nightletter.apis.service;

import com.nightletter.db.entity.Diary;
import com.nightletter.db.repository.DiaryRepository;
import com.nightletter.db.request.DiaryRequest;
import com.nightletter.db.request.DiaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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
}
