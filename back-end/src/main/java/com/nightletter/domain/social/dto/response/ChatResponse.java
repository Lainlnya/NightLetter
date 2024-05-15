package com.nightletter.domain.social.dto.response;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.social.entity.Chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
public class ChatResponse {

	Long chatId;
	Integer senderId;
	String nickname;
	String profileImgUrl;
	LocalDateTime sendTime;
	String message;
	boolean sentByMe;

	public static ChatResponse of(Chat chat, Integer userId) {
		Member sender = chat.getSender();

		return ChatResponse.builder()
			.chatId(chat.getId())
			.senderId(sender.getMemberId())
			.nickname(sender.getNickname())
			.profileImgUrl(chat.getSender().getProfileImgUrl())
			.sendTime(chat.getSendTime())
			.message(chat.getMessage())
			.sentByMe(chat.getSender().getMemberId().equals(userId))
			.build();
	}

}
