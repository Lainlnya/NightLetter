package com.nightletter.domain.diary.entity;

import java.time.LocalDate;
import java.util.Objects;

import com.nightletter.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class RecommendedDiary {
	@Id
	@Column(name = "recommend_id")
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary_id")
	private Diary diary;

	@Column
	private LocalDate scrappedDate;


	// 별도의 컬럼으로 저장.
	// 성능 테스트 예정.
	// @Column
	// private boolean isScrapped;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;

		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}

		RecommendedDiary recommend = (RecommendedDiary) obj;

		return recommend.getDiary().getDiaryId().equals(this.diary.getDiaryId())
			&& recommend.getMember().getMemberId().equals(this.getMember().getMemberId()) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(member, diary);
	}
}
