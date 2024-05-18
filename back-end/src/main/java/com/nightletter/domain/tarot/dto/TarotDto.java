package com.nightletter.domain.tarot.dto;

import java.util.List;

import com.nightletter.domain.diary.dto.recommend.EmbedVector;
import com.nightletter.domain.diary.dto.response.TodayTarot;
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
	TarotDirection dir,
	List<EmbedVector> embedVector) {

	public static TarotDto of(Tarot tarot, TarotDirection direction) {

		return TarotDto.builder()
			.id(tarot.getId())
			.name(tarot.getName())
			.imgUrl(tarot.getImgUrl())
			.dir(direction)
			.keyword(tarot.getKeyword())
			.description(tarot.getDescription())
			.embedVector(tarot.getEmbedVector())
			.build();
	}

}
