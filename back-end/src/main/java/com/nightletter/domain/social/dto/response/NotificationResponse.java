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

}
