package com.nightletter.domain.social.api;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.nightletter.domain.social.dto.request.ChatRequest;
import com.nightletter.domain.social.dto.response.ChatResponse;

@Controller
public class ChatController {

	@MessageMapping("/{roomId}")
	@SendTo("/room/{roomId}")
	public ChatResponse greeting(
		@DestinationVariable("roomId") Integer roomId,
		@Payload ChatRequest request) throws Exception {
			System.out.println(roomId);
			System.out.println(request);

			return ChatResponse.builder()
				.chatId(-1L)
				.message(request.getMessage())
				.nickname("예롬예롬")
				.senderId(3)
				.sendTime(LocalDateTime.now())
				.sentByMe(new Random().nextBoolean())
				.build();
	}
}
