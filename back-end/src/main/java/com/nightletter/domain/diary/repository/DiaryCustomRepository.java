package com.nightletter.domain.diary.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.dto.response.DiaryScrapResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.member.entity.Member;

public interface DiaryCustomRepository {

	List<Diary> findDiariesByMember(Member member, DiaryListRequest request);

	List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId, Member member);
	Page<DiaryScrapResponse> findScrappedDiaries(Integer memberId, Integer pageNo);

}
