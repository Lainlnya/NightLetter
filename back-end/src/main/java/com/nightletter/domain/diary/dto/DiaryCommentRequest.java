package com.nightletter.domain.diary.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryCommentRequest implements Serializable {

	private String model;
	private String prompt;
	@JsonProperty("max_tokens")
	private Integer maxTokens;
	private Double temperature;
	@JsonProperty("top_p")
	private Double topP;

	@Builder
	public DiaryCommentRequest(String model, String prompt,
		Integer maxTokens, Double temperature,
		Double topP) {
		this.model = model;
		this.prompt = prompt;
		this.maxTokens = maxTokens;
		this.temperature = temperature;
		this.topP = topP;
	}
}