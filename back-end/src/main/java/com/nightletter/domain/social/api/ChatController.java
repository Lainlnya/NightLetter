package com.nightletter.domain.social.api;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reactivestreams.Subscriber;
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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final ChatService chatService;

	@Value("${chat.test-profile}")
	private String testProfileImg;

	@MessageMapping("/{roomId}")
	@SendTo("/room/{roomId}")
	public ChatResponse greeting(
		@DestinationVariable("roomId") Integer roomId,
		@Payload ChatRequest request) throws Exception {
			System.out.println(roomId);
			System.out.println(request);

			// TODO 임시 Response
			return ChatResponse.builder()
				.chatId(-1L)
				.message(request.getMessage())
				.nickname("예롬예롬")
				.profileImgUrl(testProfileImg + ThreadLocalRandom.current().nextInt(0, 4) + ".jpg")
				.senderId(3)
				.sendTime(LocalDateTime.now())
				.sentByMe(new Random().nextBoolean())
				.build();
	}

	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String destination = headerAccessor.getDestination();

		Pattern pattern = Pattern.compile("/room/(\\d+)(\\?)?"); // 정규 표현식
		Matcher matcher = pattern.matcher(destination);

		if (matcher.find()) {
			String number = matcher.group(1); // 첫 번째 캡처 그룹(숫자) 추출
			System.out.println("Extracted number: " + number);
		} else {
			// TODO 예외 처리 필요.
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
