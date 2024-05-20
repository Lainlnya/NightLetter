package com.nightletter.domain.diary.dto.response;

import java.time.LocalDate;
import java.util.Objects;

import com.nightletter.global.utils.Nickname;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DiaryScrapResponse {

	// TODO 유저 닉네임 분리.

	Long diaryId;
	String nickname;
	String content;
	String imgUrl;
	LocalDate scrappedAt;

	public DiaryScrapResponse(Long diaryId, String content, String imgUrl, LocalDate scrappedAt) {
		this.diaryId = diaryId;
		this.content = content;
		this.imgUrl = imgUrl;
		this.nickname = Nickname.createRandom();
		this.scrappedAt = scrappedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DiaryScrapResponse that = (DiaryScrapResponse)o;
		return Objects.equals(content, that.content) &&
			Objects.equals(imgUrl, that.imgUrl);
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, imgUrl);
	}

}
