package com.nightletter.domain.tarot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TarotRequest {
	int id;
	List<String> keywords;
}
