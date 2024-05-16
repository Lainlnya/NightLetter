package com.nightletter.domain.diary.repository;

import static com.nightletter.domain.diary.entity.DiaryTarotType.*;
import static com.nightletter.domain.diary.entity.QDiary.*;
import static com.nightletter.domain.diary.entity.QDiaryTarot.*;
import static com.nightletter.domain.diary.entity.QRecommendedDiary.*;
import static com.nightletter.domain.diary.entity.QScrap.*;
import static com.nightletter.domain.member.entity.QMember.*;
import static com.nightletter.domain.social.entity.QChat.*;
import static com.nightletter.domain.tarot.entity.QTarot.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.dto.response.DiaryRecResponse;
import com.nightletter.domain.diary.dto.response.DiaryScrapResponse;
import com.nightletter.domain.diary.dto.response.TodayDiaryResponse;
import com.nightletter.domain.diary.dto.response.TodayTarot;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.entity.QRecommendedDiary;
import com.nightletter.domain.diary.entity.RecommendedDiary;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.entity.TarotDirection;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

	private static final int PAGE_SIZE = 10;
	private final JPAQueryFactory queryFactory;

	@Override
	public List<RecommendDiaryResponse> findRecommendDiaries(List<Long> diariesId, Member member) {
		List<RecommendDiaryResponse> responses = queryFactory.select(Projections.constructor(RecommendDiaryResponse.class,
				diary.diaryId,
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
	public Page<DiaryScrapResponse> findScrappedDiaryPages(Integer memberId, Integer pageNo) {

		Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);

		List<DiaryScrapResponse> results = queryFactory
			.select(Projections.constructor(DiaryScrapResponse.class,
				diary.diaryId,
				diary.content,
				tarot.imgUrl,
				scrap.scrappedAt
			))
			.from(member)
			.innerJoin(member.scraps, scrap)
			.innerJoin(scrap.diary, diary)
			.innerJoin(diary.diaryTarots, diaryTarot)
			.where(diaryTarot.type.eq(DiaryTarotType.NOW))
			.innerJoin(diaryTarot.tarot, tarot)
			.orderBy(scrap.scrappedAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = Optional.ofNullable(queryFactory
			.select(scrap.countDistinct())
			.from(member)
			.innerJoin(member.scraps, scrap)
			.innerJoin(scrap.diary, diary)
			.fetchOne())
			.orElse(0L);

		return new PageImpl<>(results, pageable, count);
	}

	@Override
	public List<TodayTarot> findTodayDiary(Member member, LocalDate today) {

		// TodayDiaryResponse response = new TodayDiaryResponse();

		return queryFactory.select(Projections.constructor(
			TodayTarot.class,
			diaryTarot.tarot.id,
			diaryTarot.tarot.name,
			diaryTarot.tarot.imgUrl,
			diaryTarot.tarot.dir,
			diaryTarot.type
			))
			.from(diary)
			.innerJoin(diary.diaryTarots, diaryTarot)
			.where(diaryTarot.diary.writer.eq(member)
				.and(diary.date.eq(today)))
			.fetch();
	}

	@Override
	public List<DiaryRecResponse> findTodayDiaryRecommends(Member member, LocalDate today) {
		return queryFactory.select(
			new QRecommendedDiary(
				recommendedDiary.id,
				recommendedDiary.member,
				recommendedDiary.diary,
				// 조인 해서 조회해야 함.
				// 내가 스크랩 했는지 여부.
				new CaseBuilder()
					.when(chat.sender.memberId.eq(memberId))
					.then(true)
					.otherwise(false)
				)
			)
			.where(recommendedDiary.scrappedDate.eq(today)
				.and(recommendedDiary.member.eq(member)))
			.innerJoin(recommendedDiary.diary)

		return null;
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
