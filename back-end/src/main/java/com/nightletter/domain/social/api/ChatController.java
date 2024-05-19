package com.nightletter.domain.social.api;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.social.dto.request.ChatRequest;
import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.dto.response.GptNotificationResponse;
import com.nightletter.domain.social.dto.response.RecommendNotificationResponse;
import com.nightletter.domain.social.entity.NotificationType;
import com.nightletter.domain.social.service.ChatService;
import com.nightletter.domain.social.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/chat")
public class ChatController {

	private final ChatService chatService;
	private final NotificationService notificationService;

	@GetMapping("")
	public ResponseEntity<?> findChatByChatroomId(
		@RequestParam(name = "roomId") Integer chatroomId,
		@RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
		return ResponseEntity.ok(chatService.findChatByChatroomId(chatroomId, pageNo));
	}

	@GetMapping("/entity")
	public ResponseEntity<?> findChatByChatroom(
		@RequestParam(name = "roomId") Integer chatroomId,
		@RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
		return ResponseEntity.ok(chatService.findChatByChatroomId(chatroomId, pageNo));
	}

	private int testCall = 0;

	@GetMapping("/send-notification")
	@SendToUser("/notification")
	public void sendNotification() {
		testCall++;

		GptNotificationResponse gpt = GptNotificationResponse.builder()
			.type(NotificationType.GPT_COMMENT_ARRIVAL)
			.title(NotificationType.GPT_COMMENT_ARRIVAL.getTitle())
			.build();

		RecommendNotificationResponse rec = RecommendNotificationResponse.builder()
			.type(NotificationType.RECOMMEND_DIARIES_ARRIVAL)
			.title(NotificationType.RECOMMEND_DIARIES_ARRIVAL.getTitle())
			.build();

		notificationService.sendNotificationToUser(
			testCall % 2 == 0 ? gpt : rec
		);
	}

	@MessageMapping("/{roomId}")
	@SendTo("/room/{roomId}")
	public ChatResponse sendMessage(
		@DestinationVariable("roomId") Integer roomId,
		@Payload ChatRequest request,
		Principal principal) throws Exception {

		// int memberId = testMemberCount / 3 + 1;
		// ChatResponse response = chatService.sendMessage(memberId, roomId, request.getMessage());

		System.out.println("PRINCIPAL: " + principal);
		System.out.println("member_id: " + principal.getName());

		Integer memberId = Integer.valueOf(principal.getName());

		return chatService.sendMessage(memberId, roomId, request.getMessage());
	}

}
