package com.nightletter.domain.member.dto;

import java.time.LocalDateTime;

import com.nightletter.domain.member.entity.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberResponseDto {
	private Integer memberId;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String OAuth2Id;

	private String email;

	private String nickname;

	private String profileImgUrl;

	private Provider provider;
}