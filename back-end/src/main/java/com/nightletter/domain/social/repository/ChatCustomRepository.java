package com.nightletter.domain.social.repository;

import org.springframework.data.domain.Page;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.entity.Chatroom;

public interface ChatCustomRepository {
	Page<ChatResponse> findChatPages(int chatroomId, int pageNo, int memberId);

	Page<ChatResponse> findChatPages(Chatroom chatroom, int pageNo, Member member);
}
