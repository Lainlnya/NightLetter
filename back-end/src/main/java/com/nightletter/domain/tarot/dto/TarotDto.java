package com.nightletter.domain.tarot.dto;

import com.nightletter.domain.tarot.entity.Tarot;
import java.util.List;

import com.nightletter.domain.tarot.entity.TarotDirection;
import lombok.Builder;

@Builder
public record TarotDto(
	Integer id,
	String name,
	String imgUrl,
	String keyword,
	String description,
	TarotDirection dir, 
	List<List<Double>> vector) {

	public static TarotDto of(Tarot tarot, TarotDirection direction) {

		return TarotDto.builder()
				.id(tarot.getId())
				.name(tarot.getName())
				.imgUrl(tarot.getImgUrl())
				.dir(direction)
				.keyword(tarot.getKeyword())
				.description(tarot.getDescription())
				.vector(tarot.getVector())
				.build();
	}
}
