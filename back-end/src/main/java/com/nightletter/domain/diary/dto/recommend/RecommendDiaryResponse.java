package com.nightletter.domain.diary.dto.recommend;

import com.nightletter.global.utils.Nickname;

import lombok.Getter;

@Getter
public class RecommendDiaryResponse {

	String nickname;
	String content;
	String imgUrl;

	public RecommendDiaryResponse(String content, String imgUrl) {
		this.content = content;
		this.imgUrl = imgUrl;
		this.nickname = Nickname.createRandom();
	}
}
