package com.nightletter.domain.tarot.dto;

import com.nightletter.domain.tarot.entity.TarotDirection;

public record TarotDto(
	int id,
	String name,
	String imgUrl,
	String keyword,
	String description,
	TarotDirection dir) {
}
