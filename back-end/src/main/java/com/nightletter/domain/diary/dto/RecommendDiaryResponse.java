package com.nightletter.domain.diary.dto;

import lombok.Getter;

@Getter
public class RecommendDiaryResponse {

	String nickname;
	String content;

	public RecommendDiaryResponse(String content) {
		this.content = content;
		this.nickname = null;
	}
}
