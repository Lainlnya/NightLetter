package com.nightletter.domain.diary.repository;

import com.nightletter.domain.diary.entity.Diary;

import java.time.LocalDate;
import java.util.List;

public interface DiaryCustomRepository{

    List<Diary> findDiariesByMemberId(Integer memberId, LocalDate sttDate, LocalDate endDate);
}
