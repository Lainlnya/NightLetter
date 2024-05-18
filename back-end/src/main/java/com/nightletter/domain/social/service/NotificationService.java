package com.nightletter.domain.social.service;

import java.util.List;

import com.nightletter.domain.social.dto.response.NotificationResponse;

public interface NotificationService {

	public List<NotificationResponse> getAllNotifications();

}
