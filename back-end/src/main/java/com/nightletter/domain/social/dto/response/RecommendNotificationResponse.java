package com.nightletter.domain.social.dto.response;

import java.time.LocalDateTime;

import com.nightletter.domain.social.entity.Notification;
import com.nightletter.domain.social.entity.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class RecommendNotificationResponse extends NotificationResponse {

	@Builder
	public RecommendNotificationResponse(long notificationId, NotificationType type, LocalDateTime created_at,
		String title, String content, Boolean isRead) {
		super(notificationId, type, created_at, title, content, isRead);
	}

}
