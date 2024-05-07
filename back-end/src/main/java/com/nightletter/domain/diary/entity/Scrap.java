package com.nightletter.domain.diary.entity;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Scrap extends BaseTimeEntity {

	@Id @GeneratedValue
	@Column(name ="scrap_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "diary_id")
	private Diary diary;

	private LocalDate scrappedAt;

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		Scrap scrap = (Scrap)object;
		return Objects.equals(id, scrap.id) ||
			(Objects.equals(member.getMemberId(), scrap.member.getMemberId())
				&& Objects.equals(diary.getDiaryId(), scrap.diary.getDiaryId()));
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, member, diary);
	}
}
