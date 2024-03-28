package com.nightletter.domain.tarot.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TarotResponse {
	int id;
	List<List<Double>> keywords;
}
