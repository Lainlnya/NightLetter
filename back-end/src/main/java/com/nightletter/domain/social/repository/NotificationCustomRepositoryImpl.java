package com.nightletter.domain.social.repository;

import static com.nightletter.domain.social.entity.QNotification.*;

import java.time.LocalDateTime;
import java.util.List;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.social.dto.response.NotificationQueryResponse;
import com.nightletter.domain.social.dto.response.NotificationResponse;
import com.nightletter.domain.social.dto.response.QNotificationQueryResponse;
import com.nightletter.domain.social.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<NotificationQueryResponse> findAllNotifications(Member member, LocalDateTime todayStdTime) {
		return queryFactory.
			select(new QNotificationQueryResponse(
				notification.id,
				notification.type,
				notification.isRead
			))
			.from(notification)
			.where(notification.createdAt.after(todayStdTime)
				.and(notification.member.eq(member)))
			.orderBy(notification.createdAt.desc())
			.fetch();
			// 존재하는 서브테이블 모두와 Join
			// .innerJoin()
			// .innerJoin()
	}
}
