package com.nightletter.domain.diary.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.dto.response.DiaryRecResponse;
import com.nightletter.domain.diary.dto.response.DiaryScrapResponse;
import com.nightletter.domain.diary.dto.response.FutureTarotResponse;
import com.nightletter.domain.diary.dto.response.TodayDiaryResponse;
import com.nightletter.domain.diary.dto.response.TodayTarot;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.entity.FutureTarot;

public interface DiaryCustomRepository {

	List<Diary> findDiariesByMember(Member member, DiaryListRequest request);

	List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId, Member member);
	Page<DiaryScrapResponse> findScrappedDiaryPages(Integer memberId, Integer pageNo);

	List<TodayTarot> findTodayDiary(Member member, LocalDate today);

	List<DiaryRecResponse> findTodayDiaryRecommends(Member member, LocalDate today);

	Optional<FutureTarotResponse> findFutureTarot(Long diaryId);

	void updateDiaryGPTComment(Long diaryId, String gptComment);

}
