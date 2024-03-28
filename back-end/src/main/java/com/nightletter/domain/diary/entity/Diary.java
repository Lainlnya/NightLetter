package com.nightletter.domain.diary.entity;

import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.global.common.BaseEntity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;

import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.global.common.BaseEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
@AllArgsConstructor
@Entity
public class Diary extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long diaryId;
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", referencedColumnName = "member_id", updatable = false)
	private Member writer;
	private String content;
	// 날짜 필요 (CreatedAt과 다른 컬럼)
	private LocalDate date;
	@Enumerated(EnumType.STRING)
	private DiaryOpenType type;
	private String gptComment;
	@OneToMany(mappedBy = "diary")
	private List<DiaryTarot> diaryTarots;

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
		Tarot pastCard = null;
		Tarot nowCard = null;
		Tarot futureCard = null;

		for (DiaryTarot tarot : this.diaryTarots) {
			DiaryTarotType type = tarot.getType();

			if (type.equals(DiaryTarotType.PAST)) {
				pastCard = tarot.getTarot();
			} else if (type.equals(DiaryTarotType.NOW)) {
				nowCard = tarot.getTarot();
			} else if (type.equals(DiaryTarotType.FUTURE)) {
				futureCard = tarot.getTarot();
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

	//	public DiaryResponse toDto() {
	//		return DiaryResponse.builder()
	//			.diaryId(this.writer.getId())
	//			.content(this.content)
	//			.type(this.type)
	//			.gptComment(this.gptComment)
	//			.build();
	//	}
}

