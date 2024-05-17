package com.nightletter.domain.diary.dto.recommend;

import java.util.Objects;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.global.utils.Nickname;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RecommendDiaryResponse {

	// TODO Id 포함 수정.
	// TODO 유저 닉네임 분리.

	Long diaryId;
	String nickname;
	String content;
	String imgUrl;


	public RecommendDiaryResponse(Long diaryId, String content, String imgUrl) {
		this.diaryId = diaryId;
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
