package com.nightletter.domain.diary.dto.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class GPTResponse implements Serializable {

	private String gptComment;
	private String past_url;
	private String now_url;
	private String future_url;
}

