package com.nightletter.domain.social.dto.response;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.social.entity.Chat;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data @Builder
public class ChatResponse {

	Integer senderId;
	String nickname;
	String profileImgUrl;
	LocalDateTime sendTime;
	String message;
	boolean sentByMe;

	@QueryProjection
	public ChatResponse(Integer senderId, String nickname, String profileImgUrl, LocalDateTime sendTime, String message, boolean sentByMe) {
		this.senderId = senderId;
		this.nickname = nickname;
		this.profileImgUrl = profileImgUrl;
		this.sendTime = sendTime;
		this.message = message;
		this.sentByMe = sentByMe;
	}

	public static ChatResponse of(Chat chat, Integer userId) {
		Member sender = chat.getSender();

		return ChatResponse.builder()
			.senderId(sender.getMemberId())
			.nickname(sender.getNickname())
			.profileImgUrl(chat.getSender().getProfileImgUrl())
			.sendTime(chat.getSendTime())
			.message(chat.getMessage())
			.sentByMe(chat.getSender().getMemberId().equals(userId))
			.build();
	}

}
