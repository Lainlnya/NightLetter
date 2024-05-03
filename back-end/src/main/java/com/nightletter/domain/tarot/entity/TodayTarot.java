package com.nightletter.domain.tarot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import com.nightletter.domain.tarot.dto.TarotResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RedisHash(value = "today-tarot")
public class TodayTarot {
	// 과거 카드 : 배치 + 추가 (28일)
	// 현재 카드 : 일기 작성 시 업데이트
	// 미래 카드 : 미래 카드 조회 시 새로 생성

	@Id
	private Integer memberId;
	private TarotResponse pastCard;
	private TarotResponse nowCard;
	private TarotResponse futureCard;

	@TimeToLive
	private Long expiredTime;
}
