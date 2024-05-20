package com.nightletter.domain.social.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

	// GPT 코멘트 도착, 메세지 도착, 추천 사연 도착, 친구 요청... 확장성 고려.

	GPT_COMMENT_ARRIVAL("GPT의 답장이 도착했어요!", "TEMP_CONTENT"),
	RECOMMEND_DIARIES_ARRIVAL("당신의 하루를 위한 추천 사연이 도착했어요!", "TEMP_CONTENT"),
	FRIEND_REQUEST("친구 요청 도착!", "TEMP_CONTENT");

	private final String title;
	private final String content;

}
