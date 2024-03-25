package com.nightletter.apis.service;

import com.nightletter.db.entity.Diary;
import com.nightletter.db.request.DiaryRequest;
import com.nightletter.db.request.DiaryUpdateRequest;

public interface DiaryService {

    public Diary createDiary(DiaryRequest diaryRequest);

    public Diary updateDiary(DiaryUpdateRequest diaryUpdateRequest);
}
