package com.nightletter.domain.tarot.dto;

import java.util.List;

import com.nightletter.domain.diary.dto.EmbedVector;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TarotResponse {
	int id;
	List<EmbedVector> keywords;
}
