package com.nightletter.domain.social.dto.response;

import java.time.LocalDateTime;

import com.nightletter.domain.social.entity.Notification;
import com.nightletter.domain.social.entity.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 부모 클래스

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class NotificationResponse {

	private long notificationId;
	private NotificationType type;
	private LocalDateTime created_at;
	private String title;
	private String content;
	private Boolean isRead;

	public static NotificationResponse of(Notification notification) {
		return getNotificationResponse(notification.getType(), notification.getId(), notification.isRead(), notification.getCreatedAt());
	}

	public static NotificationResponse getNotificationResponse(NotificationType type, long notificationId, Boolean isRead,
		LocalDateTime createdAt) {
		return switch (type) {
			case GPT_COMMENT_ARRIVAL -> {
				yield GptNotificationResponse.builder()
					.notificationId(notificationId)
					.type(type)
					.title(NotificationType.GPT_COMMENT_ARRIVAL.getTitle())
					.content(NotificationType.GPT_COMMENT_ARRIVAL.getContent())
					.test("TEST")
					.isRead(isRead)
					.created_at(createdAt)
					.build();
			}
			case RECOMMEND_DIARIES_ARRIVAL -> {
				yield RecommendNotificationResponse.builder()
					.notificationId(notificationId)
					.type(type)
					.title(NotificationType.RECOMMEND_DIARIES_ARRIVAL.getTitle())
					.content(NotificationType.RECOMMEND_DIARIES_ARRIVAL.getContent())
					.isRead(isRead)
					.created_at(createdAt)
					.build();
			}
			default -> null;
		};
	}

}
