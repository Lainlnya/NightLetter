package com.nightletter.domain.tarot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import com.nightletter.domain.tarot.dto.TarotDto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@RedisHash(value = "past-tarot")
public class PastTarot {
	@Id
	private Integer memberId;
	private Integer tarotId;
	private TarotDirection direction;

	@TimeToLive
	private Long expiredTime;
}
