package com.nightletter.domain.social.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.social.dto.response.GptNotificationResponse;
import com.nightletter.domain.social.dto.response.NotificationQueryResponse;
import com.nightletter.domain.social.dto.response.NotificationResponse;
import com.nightletter.domain.social.entity.Notification;
import com.nightletter.domain.social.entity.NotificationType;
import com.nightletter.domain.social.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

	private final SimpMessagingTemplate messagingTemplate;
	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	@Override
	public List<NotificationResponse> getAllNotifications() {

		LocalDateTime todayStdTime = LocalDateTime.of(getToday(), LocalTime.of(4, 0));

		System.out.println(todayStdTime);

		List<NotificationQueryResponse> notifications = notificationRepository.findAllNotifications(getCurrentMember());

		return notifications.stream().map(NotificationQueryResponse::toResponse).toList();
	}

	/**
	 * Method For Test (Not Used)
	 * @param notification
	 */
	@Override
	public void sendNotificationToUser(NotificationResponse notification) {
		messagingTemplate.convertAndSendToUser(
			String.valueOf(getCurrentMemberId()),
			"/notification",
			notification.getTitle()
		);
	}

	@Override
	public void sendNotificationToUser(NotificationType type) {
		Notification notification = Notification.builder()
			.member(getCurrentMember())
			.type(type)
			.createdAt(LocalDateTime.now())
			.isRead(false)
			.build();

		notificationRepository.save(notification);

		messagingTemplate.convertAndSendToUser(
			String.valueOf(getCurrentMemberId()),
			"/notification",
			type.getTitle()
		);
	}



	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String)authentication.getPrincipal());
	}

	private Member getCurrentMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return memberRepository.findByMemberId(Integer.parseInt((String)authentication.getPrincipal()));
	}

	private LocalDate getToday() {
		return LocalTime.now().isAfter(LocalTime.of(4, 0)) ?
			LocalDate.now() : LocalDate.now().minusDays(1);
	}

}
