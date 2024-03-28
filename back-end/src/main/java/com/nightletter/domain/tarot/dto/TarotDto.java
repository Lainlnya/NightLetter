package com.nightletter.domain.tarot.dto;

import java.util.List;

import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.Getter;

@Getter
public record TarotDto(
	Integer id,
	String name,
	String imgUrl,
	String keyword,
	String description,
	TarotDirection dir,
	List<List<Double>> vector) {
}
