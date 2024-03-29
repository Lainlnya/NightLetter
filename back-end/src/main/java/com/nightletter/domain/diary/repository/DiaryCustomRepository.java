package com.nightletter.domain.diary.repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.member.entity.Member;

import java.time.LocalDate;
import java.util.List;

public interface DiaryCustomRepository {
    List<Diary> findDiariesByMember(Member member, LocalDate sttDate, LocalDate endDate);
}
