package com.nightletter.domain.diary.repository;

import static com.nightletter.domain.diary.entity.QDiary.*;
import static com.nightletter.domain.diary.entity.QDiaryTarot.*;
import static com.nightletter.domain.tarot.entity.QTarot.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryRequestDirection;
import com.nightletter.domain.diary.dto.RecommendDiaryResponse;
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
		return queryFactory.select(Projections.constructor(RecommendDiaryResponse.class,
				diary.content,
				tarot.imgUrl
			))
			.from(diary)
			.innerJoin(diary.diaryTarots, diaryTarot)
			.on(diaryTarot.type.eq(DiaryTarotType.NOW))
			.innerJoin(diaryTarot.tarot,tarot)
			.where(diary.diaryId.in(diariesId)
				.and(diary.type.eq(DiaryOpenType.PUBLIC))
				.and(diary.writer.ne(member)))
			.fetch();
	}

	// 수정 예정
	@Override
	public List<Diary> findDiariesByMemberInDir(Member member, DiaryListRequest request) {
		List<Diary> diaries = new LinkedList<>();

		LocalDate queryDate = request.getDate();
		DiaryRequestDirection dir = request.getDirection();

		Integer limitSize = request.getSize();
		if (dir.equals(DiaryRequestDirection.BEFORE) || dir.equals(DiaryRequestDirection.BOTH)) {
			diaries.addAll(queryFactory.select(diary)
				.from(diary)
				.where(diary.writer.eq(member)
					.and(diary.date.loe(queryDate)))
				.orderBy(diary.date.asc())
				.limit(limitSize)
				.fetch()
			);
		}

		if (dir.equals(DiaryRequestDirection.BOTH) &&
			! diaries.isEmpty() &&
			diaries.get(diaries.size()-1).getDate().isEqual(queryDate)) {
			diaries.remove(diaries.get(diaries.size()-1));
		}

		if (dir.equals(DiaryRequestDirection.AFTER) || dir.equals(DiaryRequestDirection.BOTH)) {
			diaries.addAll(queryFactory.select(diary)
				.from(diary)
				.where(diary.writer.eq(member)
					.and(diary.date.goe(queryDate)))
				.orderBy(diary.date.asc())
				.limit(limitSize)
				.fetch()
			);
		}

		return diaries;
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
