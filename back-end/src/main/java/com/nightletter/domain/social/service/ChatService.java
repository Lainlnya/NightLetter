package com.nightletter.domain.social.service;

import org.springframework.stereotype.Service;

import com.nightletter.domain.social.dto.request.ChatRequest;
import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.entity.Chat;
import com.nightletter.domain.social.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
public interface ChatService {

	public ChatResponse sendMessage(Integer roomId, String message);

	public void joinChatroom(Integer roomId);
}
