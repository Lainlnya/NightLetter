package com.nightletter.domain.social.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.nightletter.domain.social.dto.response.ChatResponse;

@Service
public interface ChatService {

	public ChatResponse sendMessage(Integer memberId, Integer roomId, String message);
	public ChatResponse sendMessage(Integer roomId, String message);
	public Page<ChatResponse> findChatByChatroomId(int chatroomId, int pageNo);
	public Page<ChatResponse> findChatByChatroom(int chatroomId, int pageNo);

	public void joinChatroom(Integer roomId);
}
