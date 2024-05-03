package com.nightletter.domain.diary.dto.response;

import java.io.Serializable;
import java.util.List;

import com.nightletter.domain.diary.dto.request.GPTRequest;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
public class DiaryCommentResponse implements Serializable {

	private List<Choice> choices;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Choice {
		private int index;
		private GPTRequest message;

	}
}

