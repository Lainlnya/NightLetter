package com.nightletter.domain.social.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.social.dto.response.GptNotificationResponse;
import com.nightletter.domain.social.dto.response.NotificationResponse;
import com.nightletter.domain.social.dto.response.RecommendNotificationResponse;
import com.nightletter.domain.social.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/chat")
public class ChatController {

	private final ChatService chatService;

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

}
