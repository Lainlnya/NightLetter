package com.nightletter.domain.social.service;

import org.springframework.stereotype.Service;

import com.nightletter.domain.social.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
	private final ChatRepository chatRepository;
}
