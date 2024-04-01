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
	TarotDirection dir;
	String keyword;
	String desc;

	public static PastTarotResponse of(Tarot tarot, TarotDirection direction) {

		return PastTarotResponse.builder()
			.name(tarot.getName())
			.imgUrl(tarot.getImgUrl())
			.type(DiaryTarotType.PAST)
			.dir(direction)
			.keyword(tarot.getKeyword())
			.desc(tarot.getDescription())
			.build();
	}
}
