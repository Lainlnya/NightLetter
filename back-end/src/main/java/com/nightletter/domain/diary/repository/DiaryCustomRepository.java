package com.nightletter.domain.diary.repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.member.entity.Member;

import java.time.LocalDate;
import java.util.List;

import com.nightletter.domain.diary.dto.RecommendDiaryResponse;
import com.nightletter.domain.diary.entity.Diary;

public interface DiaryCustomRepository {

	List<Diary> findDiariesByMember(Member member, LocalDate sttDate, LocalDate endDate);
	List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId);
}
