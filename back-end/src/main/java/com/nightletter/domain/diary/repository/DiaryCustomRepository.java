package com.nightletter.domain.diary.repository;

import java.time.LocalDate;
import java.util.List;

import com.nightletter.domain.diary.dto.RecommendDiaryResponse;
import com.nightletter.domain.diary.entity.Diary;

public interface DiaryCustomRepository {

	List<Diary> findDiariesByMemberId(Long memberId, LocalDate sttDate, LocalDate endDate);
	List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId);
}
