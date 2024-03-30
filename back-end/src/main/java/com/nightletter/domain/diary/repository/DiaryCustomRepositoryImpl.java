package com.nightletter.domain.diary.repository;

import static com.nightletter.domain.diary.entity.QDiary.*;

import java.time.LocalDate;
import java.util.List;

import com.nightletter.domain.diary.dto.RecommendDiaryResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId) {
		return queryFactory.select(Projections.constructor(RecommendDiaryResponse.class,
				diary.content
			))
			.from(diary)
			.where(diary.diaryId.in(diariesId)
				.and(diary.type.eq(DiaryOpenType.PUBLIC)))
			.fetch();
	}

	@Override
	public List<Diary> findDiariesByMember(Member member, LocalDate sttDate, LocalDate endDate) {
		return queryFactory.select(diary)
			.from(diary)
			.where(diary.writer.eq(member)
				.and(diary.date.between(sttDate, endDate)))
			.fetch();
	}
}
