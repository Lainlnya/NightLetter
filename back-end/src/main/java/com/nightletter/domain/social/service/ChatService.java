package com.nightletter.domain.social.service;

import org.springframework.stereotype.Service;

import com.nightletter.domain.social.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
public interface ChatService {

	public void joinChatroom(String destination);
}
