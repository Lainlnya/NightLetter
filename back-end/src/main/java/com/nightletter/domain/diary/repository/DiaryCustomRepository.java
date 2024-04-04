package com.nightletter.domain.diary.repository;

import java.time.LocalDate;
import java.util.List;

import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.member.entity.Member;

public interface DiaryCustomRepository {

	List<Diary> findDiariesByMember(Member member, LocalDate sttDate, LocalDate endDate);

	List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId, Member member);

	List<Diary> findDiariesByMemberInDir(Member member, DiaryListRequest request);
}
