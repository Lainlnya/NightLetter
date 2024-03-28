package com.nightletter.domain.diary.repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryType;
import com.nightletter.domain.member.entity.Member;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nightletter.domain.diary.entity.Diary;

public interface DiaryCustomRepository {
	List<Diary> findDiariesByMemberId(Long memberId, LocalDate sttDate, LocalDate endDate);
    List<Diary> findDiariesByMember(Member member, LocalDate sttDate, LocalDate endDate);

    long updateDiaryType(Long diaryId, DiaryType diaryType);
}
