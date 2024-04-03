package com.nightletter.global.security.token;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash(value = "refresh-token")
public class RefreshToken {
	@Id
	private Integer memberId;
	private String refreshToken;

	@TimeToLive
	private Long expiredTime;
}
