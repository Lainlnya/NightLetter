package com.nightletter.domain.tarot.dto;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PastTarotResponse {
	String name;
	String imgUrl;
	DiaryTarotType type;
	String keyword;
	String description;
	TarotDirection dir;

	public static PastTarotResponse of(Tarot tarot, TarotDirection direction) {

		return PastTarotResponse.builder()
			.name(tarot.getName())
			.imgUrl(tarot.getImgUrl())
			.type(DiaryTarotType.PAST)
			.dir(direction)
			.keyword(tarot.getKeyword())
			.description(tarot.getDescription())
			.build();
	}
}
