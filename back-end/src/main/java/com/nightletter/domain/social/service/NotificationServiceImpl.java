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
import com.nightletter.domain.social.dto.response.NotificationQueryResponse;
import com.nightletter.domain.social.dto.response.NotificationResponse;
import com.nightletter.domain.social.entity.Notification;
import com.nightletter.domain.social.repository.NotificationCustomRepository;
import com.nightletter.domain.social.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

	private final SimpMessagingTemplate simpleMessageTemplate;
	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	@Override
	public List<NotificationResponse> getAllNotifications() {

		LocalDateTime todayStdTime = LocalDateTime.of(getToday(), LocalTime.of(4, 0));

		List<NotificationQueryResponse> notifications = notificationRepository.findAllNotifications(getCurrentMember(),
			todayStdTime);

		// TODO notifications 요소 하나씩 Response로 처리.
		// TODO notification Type 마다 다르게 설정해야함.
		return null;
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
