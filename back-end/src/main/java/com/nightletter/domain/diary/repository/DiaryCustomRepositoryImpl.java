package com.nightletter.domain.diary.repository;

import static com.nightletter.domain.diary.entity.QDiary.*;
import static com.nightletter.domain.diary.entity.QDiaryTarot.*;
import static com.nightletter.domain.tarot.entity.QTarot.*;

import java.util.List;
import java.util.stream.Collectors;

import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId, Member member) {
		List<RecommendDiaryResponse> responses = queryFactory.select(Projections.constructor(RecommendDiaryResponse.class,
				diary.content,
				tarot.imgUrl
			))
			.from(diary)
			.innerJoin(diary.diaryTarots, diaryTarot)
			.where(diaryTarot.type.eq(DiaryTarotType.NOW))
			.innerJoin(diaryTarot.tarot, tarot)
			.where(diary.diaryId.in(diariesId)
				.and(diary.type.eq(DiaryOpenType.PUBLIC))
				.and(diary.writer.ne(member)))
			.fetch();

		return responses.stream()
			.distinct()
			.collect(Collectors.toList());
	}

	@Override
	public List<Diary> findDiariesByMember(Member member, DiaryListRequest request) {
		return queryFactory.select(diary)
			.from(diary)
			.where(diary.writer.eq(member)
				.and(diary.date.between(request.getSttDate(), request.getEndDate())))
			.orderBy(diary.date.asc())
			.fetch();
	}
}
