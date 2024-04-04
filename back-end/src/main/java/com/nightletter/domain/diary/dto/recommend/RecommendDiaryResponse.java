package com.nightletter.domain.diary.dto.recommend;

import java.util.Objects;

import com.nightletter.global.utils.Nickname;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RecommendDiaryResponse {

	String nickname;
	String content;
	String imgUrl;

	public RecommendDiaryResponse(String content, String imgUrl) {
		this.content = content;
		this.imgUrl = imgUrl;
		this.nickname = Nickname.createRandom();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RecommendDiaryResponse that = (RecommendDiaryResponse)o;
		return Objects.equals(content, that.content) &&
			Objects.equals(imgUrl, that.imgUrl);
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, imgUrl);
	}

}
