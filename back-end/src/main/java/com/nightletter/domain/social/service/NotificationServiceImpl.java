package com.nightletter.domain.social.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.social.dto.response.GptNotificationResponse;
import com.nightletter.domain.social.dto.response.NotificationQueryResponse;
import com.nightletter.domain.social.dto.response.NotificationResponse;
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

	@Override
	public void sendNotificationToUser(String userId, NotificationResponse notification) {
		messagingTemplate.convertAndSendToUser(userId, "/notification",
			GptNotificationResponse.builder()
				.notificationId(1)
				.type(NotificationType.GPT_COMMENT_ARRIVAL)
				.title("GPT 받아라")
				.content("GPT 답장 받아라")
				.created_at(LocalDateTime.now())
				.test("test입니다.")
				.build()
			);
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
