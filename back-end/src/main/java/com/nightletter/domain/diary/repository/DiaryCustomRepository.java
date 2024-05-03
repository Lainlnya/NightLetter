package com.nightletter.domain.diary.repository;

import java.util.List;

import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.member.entity.Member;

public interface DiaryCustomRepository {

	List<Diary> findDiariesByMember(Member member, DiaryListRequest request);

	List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId, Member member);

}
