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

	@GetMapping("/send-notification")
	@SendToUser("/notification")
	public void sendNotification(SimpMessageHeaderAccessor headerAccessor) {
		String userId = (String) headerAccessor.getSessionAttributes().get("memberId");
		notificationService.sendNotificationToUser(userId, null);
	}

}
