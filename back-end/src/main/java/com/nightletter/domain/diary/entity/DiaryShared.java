package com.nightletter.domain.diary.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.Data;

@Data
@RedisHash(value = "shared-diary")
public class DiaryShared {

	@Id
	Long diaryId;
	// 공유자
	@Indexed
	Integer memberId;
	private List<Long> diaries;
	@TimeToLive
	private Long expiredTime;

	@Builder
	public DiaryShared(List<Long> diaries, Integer memberId) {
		this.diaries = diaries;
		this.memberId = memberId;
		this.expiredTime = 172800L;
	}
}
