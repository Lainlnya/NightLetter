package com.nightletter.domain.social.dto.response;

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

	/*
	 * 추가적으로 값 추가.
	 * private String test;
	 */

	@QueryProjection
	public NotificationQueryResponse(long notificationId, NotificationType type, Boolean isRead) {
		this.notificationId = notificationId;
		this.type = type;
		this.isRead = isRead;
	}

	public NotificationResponse toResponse() {
		return switch (this.type) {
			case GPT_COMMENT_ARRIVAL -> {
				yield GptNotificationResponse.builder()
					.notificationId(this.notificationId)
					.title(NotificationType.GPT_COMMENT_ARRIVAL.getTitle())
					.content(NotificationType.GPT_COMMENT_ARRIVAL.getContent())
					.test("TEST")
					.isRead(this.isRead)
					.build();
			}
			case RECOMMEND_DIARIES_ARRIVAL -> {
				yield RecommendNotificationResponse.builder()
					.notificationId(this.notificationId)
					.title(NotificationType.RECOMMEND_DIARIES_ARRIVAL.getTitle())
					.content(NotificationType.RECOMMEND_DIARIES_ARRIVAL.getContent())
					.isRead(this.isRead)
					.build();
			}
			default -> null;
		};
	}

}
