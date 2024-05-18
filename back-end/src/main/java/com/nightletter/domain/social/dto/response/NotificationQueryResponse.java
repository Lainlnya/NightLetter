package com.nightletter.domain.social.dto.response;

import com.nightletter.domain.social.entity.NotificationType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
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

}
