package com.nightletter.domain.social.service;

import java.util.List;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.social.dto.response.NotificationResponse;
import com.nightletter.domain.social.entity.NotificationType;

public interface NotificationService {

	public List<NotificationResponse> getAllNotifications();

	public void sendNotificationToUser(NotificationResponse notification);
	public void sendNotificationToUser(NotificationType type, Member member);
}
