package com.nightletter.domain.tarot.dto;

import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.entity.TarotDirection;
import lombok.Builder;

@Builder
public record TarotDto(
	int id,
	String name,
	String imgUrl,
	String keyword,
	String description,
	TarotDirection dir) {

	public static TarotDto of(Tarot tarot, TarotDirection direction) {

		return TarotDto.builder()
				.id(tarot.getId())
				.name(tarot.getName())
				.imgUrl(tarot.getImgUrl())
				.dir(direction)
				.keyword(tarot.getKeyword())
				.description(tarot.getDescription())
				.build();
	}
}
