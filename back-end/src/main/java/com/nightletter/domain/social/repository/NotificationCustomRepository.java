package com.nightletter.domain.social.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.social.dto.response.NotificationQueryResponse;

public interface NotificationCustomRepository {

	public List<NotificationQueryResponse> findAllNotifications(Member member, LocalDateTime todayStdTime);
}
