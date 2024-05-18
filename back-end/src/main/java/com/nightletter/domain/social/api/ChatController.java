package com.nightletter.domain.social.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.social.dto.response.GptNotificationResponse;
import com.nightletter.domain.social.dto.response.NotificationResponse;
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

}
