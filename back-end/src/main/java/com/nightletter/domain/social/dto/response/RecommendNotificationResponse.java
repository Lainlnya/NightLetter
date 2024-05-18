package com.nightletter.domain.social.dto.response;

import com.nightletter.domain.social.entity.Notification;

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
	public RecommendNotificationResponse(long notificationId, String title, String content, boolean isRead) {
		super(notificationId, title, content, isRead);
	}

}
