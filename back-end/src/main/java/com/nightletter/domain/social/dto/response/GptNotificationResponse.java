package com.nightletter.domain.social.dto.response;

import com.nightletter.domain.social.entity.Notification;

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
	public GptNotificationResponse(long notificationId, String title, String content, boolean isRead, String test) {
		super(notificationId, title, content, isRead);
		this.test = test;
	}

}
