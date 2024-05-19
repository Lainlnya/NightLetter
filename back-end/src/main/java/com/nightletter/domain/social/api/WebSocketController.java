package com.nightletter.domain.social.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.nightletter.domain.social.dto.request.ChatRequest;
import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.service.ChatService;
import com.nightletter.domain.social.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class WebSocketController {

	private final ChatService chatService;
	private final NotificationService notificationService;

	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String destination = headerAccessor.getDestination();

		System.out.println("TEST1: " + event.getUser().toString());
		System.out.println("TEST2: " + event.getSource().toString());
		System.out.println("TEST3: " + event.getMessage());

		// TODO null 처리 .
		if (destination == null) {

		}

		if (destination.equals("/notification")) {
			return ;
		}

		Pattern pattern = Pattern.compile("/room/(\\d+)(\\?)?"); // 정규 표현식
		Matcher matcher = pattern.matcher(destination);

		if (matcher.find()) {
			Integer roomId = Integer.parseInt(matcher.group(1)); // 첫 번째 캡처 그룹(숫자) 추출
			System.out.println("Extracted number: " + roomId);
		} else {
			System.out.println("No number found in the string.");
		}

	}

	@EventListener
	public void handleDisconnectEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		System.out.println(headerAccessor.toString());

		System.out.println("disconnected");
	}

}
