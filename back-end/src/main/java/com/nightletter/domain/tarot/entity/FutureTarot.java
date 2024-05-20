package com.nightletter.domain.tarot.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RedisHash(value = "future-tarot")
public class FutureTarot {

	@Id
	private Integer memberId;
	private Boolean flipped;

	@TimeToLive
	private Long expiredTime;

	public static FutureTarot unflipped(Integer memberId) {
		return new FutureTarot(memberId, false, expiredTime());
	}

	public static FutureTarot flipped(Integer memberId) {
		return new FutureTarot(memberId, true, expiredTime());
	}

	private static Long expiredTime() {
		LocalDateTime expiredTime = LocalDateTime.of(getToday().plusDays(1), LocalTime.of(4, 0));

		return expiredTime.toEpochSecond(ZoneOffset.UTC)
			- LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	}

	private static LocalDate getToday() {
		return LocalTime.now().isAfter(LocalTime.of(4, 0)) ?
			LocalDate.now() : LocalDate.now().minusDays(1);
	}

}
