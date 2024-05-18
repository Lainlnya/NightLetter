package com.nightletter.domain.social.dto.response;

import java.time.LocalDateTime;

import com.nightletter.domain.social.entity.NotificationType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationQueryResponse {
	private long notificationId;
	private NotificationType type;
	private Boolean isRead;
	private LocalDateTime created_at;

	/*
	 * 추가적으로 값 추가.
	 * private String test;
	 */

	@QueryProjection
	public NotificationQueryResponse(long notificationId, NotificationType type, Boolean isRead, LocalDateTime created_at) {
		this.notificationId = notificationId;
		this.type = type;
		this.isRead = isRead;
		this.created_at = created_at;
	}

	public NotificationResponse toResponse() {
		return switch (this.type) {
			case GPT_COMMENT_ARRIVAL -> {
				yield GptNotificationResponse.builder()
					.notificationId(this.notificationId)
					.type(this.type)
					.title(NotificationType.GPT_COMMENT_ARRIVAL.getTitle())
					.content(NotificationType.GPT_COMMENT_ARRIVAL.getContent())
					.test("TEST")
					.isRead(this.isRead)
					.created_at(this.created_at)
					.build();
			}
			case RECOMMEND_DIARIES_ARRIVAL -> {
				yield RecommendNotificationResponse.builder()
					.notificationId(this.notificationId)
					.type(this.type)
					.title(NotificationType.RECOMMEND_DIARIES_ARRIVAL.getTitle())
					.content(NotificationType.RECOMMEND_DIARIES_ARRIVAL.getContent())
					.isRead(this.isRead)
					.created_at(this.created_at)
					.build();
			}
			default -> null;
		};
	}

}
