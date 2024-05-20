package com.nightletter.domain.diary.dto.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DiaryCommentRequest  {

	private String model;
	private List<GPTRequest> messages;

	public DiaryCommentRequest(String model, String prompt) {
		this.model = model;
		this.messages = new ArrayList<>();
		this.messages.add(new GPTRequest("system", "너는 '해요체'로 대답하는 챗봇이야. 사용자가 타로카드 1장과 일기를 주면, 타로카드의 간략한 의미를 설명하고, 일기에 공감해 주면서 미래를 점 봐줘야해. 200자 이내로." ));
		this.messages.add(new GPTRequest("user", prompt));

	}
}