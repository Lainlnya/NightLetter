package com.nightletter.domain.diary.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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

