package com.nightletter.domain.diary.dto;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmbedVector {
	private List<Double> embed;


	public String convertString(){
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writeValueAsString(embed);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
