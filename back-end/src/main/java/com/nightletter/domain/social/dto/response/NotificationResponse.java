package com.nightletter.domain.social.dto.response;

import com.nightletter.domain.social.entity.Notification;

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
	private String title;
	private String content;
	private Boolean isRead;

}
