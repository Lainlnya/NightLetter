package com.nightletter.domain.social.dto.response;

import java.time.LocalDateTime;

import com.nightletter.domain.social.entity.Notification;
import com.nightletter.domain.social.entity.NotificationType;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class GptNotificationResponse extends NotificationResponse {
	private String test;


	@Builder
	public GptNotificationResponse(long notificationId, NotificationType type, LocalDateTime created_at,
		String title, String content, Boolean isRead, String test) {
		super(notificationId, type, created_at, title, content, isRead);
		this.test = test;
	}

}
