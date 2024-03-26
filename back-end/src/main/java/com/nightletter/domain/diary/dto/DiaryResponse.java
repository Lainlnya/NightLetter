package com.nightletter.domain.diary.dto;

import com.nightletter.domain.diary.entity.DiaryType;

import lombok.Builder;
import lombok.Data;

@Data
public class DiaryResponse {

	private Integer id;
	private String content;
	private DiaryType type;
	private String gptComment;

	@Builder
	public DiaryResponse(Integer id, String content, DiaryType type, String gptComment) {
		this.id = id;
		this.content = content;
		this.type = type;
		this.gptComment = gptComment;
	}
}
