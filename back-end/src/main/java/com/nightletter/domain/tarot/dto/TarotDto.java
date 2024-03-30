package com.nightletter.domain.tarot.dto;

import java.util.List;

import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.entity.TarotDirection;

public record TarotDto(
	Integer id,
	String name,
	String imgUrl,
	String keyword,
	String description,
	TarotDirection dir,
	List<List<Double>> vector) {

	public Tarot toEntity() {
		return Tarot.builder().id(id)
			.name(name)
			.imgUrl(imgUrl)
			.keyword(keyword)
			.description(description)
			.dir(dir)
			.vector(vector).
			build();
	}
}
