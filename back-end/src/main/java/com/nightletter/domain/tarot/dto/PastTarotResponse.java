package com.nightletter.domain.tarot.dto;

import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PastTarotResponse {
	Integer id;
	String name;
	String imgUrl;
	String keyword;
	String description;
	TarotDirection dir;

	public static PastTarotResponse of(Tarot tarot, TarotDirection direction) {

		return PastTarotResponse.builder()
			.id(tarot.getId())
			.name(tarot.getName())
			.imgUrl(tarot.getImgUrl())
			.dir(direction)
			.keyword(tarot.getKeyword())
			.description(tarot.getDescription())
			.build();
	}
}
