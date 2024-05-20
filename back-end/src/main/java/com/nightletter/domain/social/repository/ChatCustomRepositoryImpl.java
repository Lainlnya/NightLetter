package com.nightletter.domain.social.repository;

import static com.nightletter.domain.social.entity.QChat.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.dto.response.QChatResponse;
import com.nightletter.domain.social.entity.Chatroom;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatCustomRepositoryImpl implements ChatCustomRepository {

	static final int PAGE_SIZE = 30;
	private final JPAQueryFactory queryFactory;

	// 쿼리 수 비교.
	// 이후 offset 0로 ...
	@Override
	public Page<ChatResponse> findChatPages(int chatroomId, int pageNo, int memberId) {

		// TODO 함수 전역 처리
		LocalDateTime todayStdTime = LocalDateTime.of(getToday(), LocalTime.of(4, 0));

		Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);

		List<ChatResponse> results = queryFactory
			.select(
				new QChatResponse(
				chat.sender.memberId,
				chat.sender.nickname,
				chat.sender.profileImgUrl,
				chat.sendTime,
				chat.message,
				new CaseBuilder()
					.when(chat.sender.memberId.eq(memberId))
					.then(true)
					.otherwise(false)
				)
			)
			.from(chat)
			.where(
				chat.sendTime.after(todayStdTime)
					.and(chat.chatroom.id.eq(chatroomId)))
			.orderBy(chat.sendTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = Optional.ofNullable(queryFactory
				.select(chat.countDistinct())
				.from(chat)
				.where(chat.chatroom.id.eq(chatroomId))
				.fetchOne()
			)
			.orElse(0L);

		return new PageImpl<>(results, pageable, count);
	}

	@Override
	public Page<ChatResponse> findChatPages(Chatroom chatroom, int pageNo, Member member) {
		Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);

		List<ChatResponse> results = queryFactory
			.select(
				new QChatResponse(
					chat.sender.memberId,
					chat.sender.nickname,
					chat.sender.profileImgUrl,
					chat.sendTime,
					chat.message,
					new CaseBuilder()
						.when(chat.sender.eq(member))
						.then(true)
						.otherwise(false)
				)
			)
			.from(chat)
			.where(chat.chatroom.eq(chatroom))
			.orderBy(chat.sendTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = Optional.ofNullable(queryFactory
				.select(chat.countDistinct())
				.from(chat)
				.where(chat.chatroom.eq(chatroom))
				.fetchOne()
			)
			.orElse(0L);

		return new PageImpl<>(results, pageable, count);
	}

	private LocalDate getToday() {
		return LocalTime.now().isAfter(LocalTime.of(4, 0)) ?
			LocalDate.now() : LocalDate.now().minusDays(1);
	}
}
