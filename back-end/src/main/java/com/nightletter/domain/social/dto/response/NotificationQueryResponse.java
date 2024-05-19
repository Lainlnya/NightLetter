package com.nightletter.domain.social.dto.response;

import static com.nightletter.domain.social.dto.response.NotificationResponse.*;

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
		return getNotificationResponse(this.type, this.notificationId, this.isRead, this.created_at);
	}

}
