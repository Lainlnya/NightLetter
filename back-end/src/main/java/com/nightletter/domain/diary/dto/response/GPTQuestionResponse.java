package com.nightletter.domain.diary.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GPTQuestionResponse implements Serializable {

	private String text;
	private Integer index;
	@JsonProperty("finish_reason")
	private String finishReason;

	@Builder
	public GPTQuestionResponse(String text, Integer index, String finishReason) {
		this.text = text;
		this.index = index;
		this.finishReason = finishReason;
	}
}