package com.nightletter.domain.diary.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.global.common.BaseTimeEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Diary SET deleted_at = now() WHERE diary_id = ?")
@SQLRestriction("deleted_at IS NULL")
@Entity
public class Diary extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long diaryId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", updatable = false)
	private Member writer;
	@Column(length = 5000)
	private String content;
	// 날짜 필요 (CreatedAt과 다른 컬럼)
	private LocalDate date;
	@Enumerated(EnumType.STRING)
	private DiaryOpenType type;
	@Column(length = 3000)
	private String gptComment;
	@OneToMany(mappedBy = "diary")
	private List<DiaryTarot> diaryTarots;
	private LocalDateTime deletedAt;

	@Nullable
	@Column(columnDefinition = "json")
	private String vector;

	public Diary() {
		super();
	}

	public Diary modifyDiaryDisclosure(DiaryOpenType diaryOpenType) {
		this.type = diaryOpenType;
		return this;
	}

	public DiaryResponse toDiaryResponse() {
		TarotDto pastCard = null;
		TarotDto nowCard = null;
		TarotDto futureCard = null;

		for (DiaryTarot tarot : this.diaryTarots) {
			DiaryTarotType type = tarot.getType();

			if (type.equals(DiaryTarotType.PAST)) {
				pastCard = TarotDto.of(tarot.getTarot(), tarot.getTarot().getDir());
			} else if (type.equals(DiaryTarotType.NOW)) {
				nowCard = TarotDto.of(tarot.getTarot(), tarot.getTarot().getDir());
			} else if (type.equals(DiaryTarotType.FUTURE)) {
				futureCard = TarotDto.of(tarot.getTarot(), tarot.getTarot().getDir());
			}
		}

		return DiaryResponse.builder()
			.writerId(this.writer.getMemberId())
			.diaryId(this.diaryId)
			.type(this.type)
			.content(this.content)
			.gptComment(this.gptComment)
			.pastCard(pastCard)
			.nowCard(nowCard)
			.futureCard(futureCard)
			.date(this.date)
			.build();
	}
}

