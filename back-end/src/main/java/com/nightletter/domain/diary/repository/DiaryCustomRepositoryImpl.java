package com.nightletter.domain.diary.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.QDiary;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Diary> findDiariesByMemberId(Long memberId, LocalDate sttDate, LocalDate endDate) {
		QDiary diary = QDiary.diary;

		List<Diary> diaries = queryFactory.select(diary)
			.where(diary.writer.id.eq(memberId)
				.and(diary.date.between(sttDate, endDate)))
			.fetch();

		return diaries;
	}
}
