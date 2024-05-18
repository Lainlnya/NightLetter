package com.nightletter.domain.tarot.repository;

import static com.nightletter.domain.diary.entity.QDiary.*;
import static com.nightletter.domain.diary.entity.QDiaryTarot.*;
import static com.nightletter.domain.tarot.entity.QTarot.*;

import java.time.LocalDate;
import java.util.Optional;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.entity.Tarot;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TarotCustomRepositoryImpl implements TarotCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Tarot> findPastTarot(LocalDate today, int memberId) {
		LocalDate pastLimitDate = today.minusDays(28);

		return Optional.ofNullable(queryFactory.select(tarot)
			.from(diary)
			.innerJoin(diary.diaryTarots, diaryTarot)
			.innerJoin(diaryTarot.tarot, tarot)
			.where(
				diary.writer.memberId.eq(memberId),
				diary.date.between(pastLimitDate, today),
				diaryTarot.type.eq(DiaryTarotType.NOW)
			)
			.orderBy(diary.date.desc())
			.limit(1)
			.fetchOne());
	}

	@Override
	public Optional<Tarot> findNowTarot(LocalDate today, int memberId) {
		return Optional.ofNullable(queryFactory.select(tarot)
			.from(diary)
			.innerJoin(diary.diaryTarots, diaryTarot)
			.innerJoin(diaryTarot.tarot, tarot)
			.where(
				diary.writer.memberId.eq(memberId),
				diary.date.eq(today),
				diaryTarot.type.eq(DiaryTarotType.NOW)
			)
			.orderBy(diary.date.desc())
			.limit(1)
			.fetchOne());
	}
}
