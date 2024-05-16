package com.nightletter.domain.social.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.nightletter.domain.social.dto.request.ChatRequest;
import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatConnController {

	private final ChatService chatService;

	@Value("${chat.test-profile}")
	private String testProfileImg;

	private int testMemberCount = 0;

	@MessageMapping("/{roomId}")
	@SendTo("/room/{roomId}")
	public ChatResponse sendMessage(
		@DestinationVariable("roomId") Integer roomId,
		@Payload ChatRequest request) throws Exception {
			System.out.println(roomId);
			System.out.println(request);

			// TODO 임시 Response 수정 필요.
			int memberId = testMemberCount / 3 + 1;

			ChatResponse response = chatService.sendMessage(memberId, roomId, request.getMessage());


			response.setProfileImgUrl(testProfileImg + memberId + ".jpg");
			response.setSentByMe(memberId == 3);
			response.setSenderId(memberId);

			testMemberCount = (++testMemberCount % 12);

			return response;
	}

	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String destination = headerAccessor.getDestination();

		// TODO null 처리 .
		if (destination == null) {

		}

		Pattern pattern = Pattern.compile("/room/(\\d+)(\\?)?"); // 정규 표현식
		Matcher matcher = pattern.matcher(destination);

		if (matcher.find()) {
			Integer roomId = Integer.parseInt(matcher.group(1)); // 첫 번째 캡처 그룹(숫자) 추출
			System.out.println("Extracted number: " + roomId);
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
