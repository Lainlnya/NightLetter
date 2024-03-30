package com.nightletter.domain.diary.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Embeddable
public class DiaryTarotId implements Serializable {

	private Long diaryId;
	private Integer tarotId;

	protected DiaryTarotId() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;

		DiaryTarotId other = (DiaryTarotId)obj;

		return this.diaryId.equals(other.diaryId)
			&& this.tarotId.equals(other.tarotId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.diaryId, this.tarotId);
	}
}
