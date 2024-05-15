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

			// TODO 임시 Response
			return ChatResponse.builder()
				.chatId(-1L)
				.message(request.getMessage())
				.nickname("예롬예롬")
				.profileImgUrl("https://i.namu.wiki/i/MCV0dofhJ6D7afd6Ajr2ZK3jXTm9fGKUcBpwNSfaz-cJWDbvtNodOd_Paqrmh7C49U4L3MAPYoE_PKAJtMKlpCsP0gnJ3n833LcCobsfgsu4S1a2HELeLZM9ZreFC-NhtD3lpyMlOgSW8idLWgC9FA.webp")
				.senderId(3)
				.sendTime(LocalDateTime.now())
				.sentByMe(new Random().nextBoolean())
				.build();
	}
}
