package com.nightletter.domain.social.service;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.entity.Chat;
import com.nightletter.domain.social.entity.Chatroom;
import com.nightletter.domain.social.entity.Participant;
import com.nightletter.domain.social.repository.ChatRepository;
import com.nightletter.domain.social.repository.ChatroomRepository;
import com.nightletter.domain.social.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

	private final ChatRepository chatRepository;
	private final ChatroomRepository chatroomRepository;
	private final ParticipantRepository participantRepository;
	private final MemberRepository memberRepository;

	@Override
	public ChatResponse sendMessage(Integer memberId, Integer roomId, String message) {
		System.out.println("sendMessageTO: " + memberId);

		Chatroom chatroom = chatroomRepository.findById(roomId)
			.orElseThrow();

		Member member = memberRepository.findByMemberId(memberId);

		Chat chat = Chat.builder()
			.message(message)
			.chatroom(chatroom)
			.sender(member)
			.sendTime(LocalDateTime.now())
			.build();

		chatRepository.save(chat);

		return ChatResponse.of(chat, memberId);
	}

	@Override
	public Page<ChatResponse> findChatByChatroomId(Integer memberId, int chatroomId, int pageNo) {

		return chatRepository.findChatPages(chatroomId, pageNo, getCurrentMemberId());
	}

	@Override
	public void joinChatroom(Integer roomId) {

		Chatroom chatroom = chatroomRepository.findById(roomId)
			.orElseThrow();

		Member member = getCurrentMember();

		Participant participant = Participant.builder()
			.chatroom(chatroom)
			.member(member)
			.participatedTime(LocalDateTime.now())
			.build();

		// TODO 동일한 유저 여러번 안들어가게 처리 확인
		// 현재 Disconn 시 삭제 기능 없음. 만들지도 미지수.
		participantRepository.save(participant);
	}

	private Member getCurrentMember() {
		return memberRepository.findByMemberId(getCurrentMemberId());
	}

	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String)authentication.getPrincipal());
	}

}
