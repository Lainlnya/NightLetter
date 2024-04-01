package com.nightletter.domain.diary.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.global.common.BaseTimeEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
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

	private LocalDate date;

	@Enumerated(EnumType.STRING)
	private DiaryOpenType type;

	@Column(length = 3000)
	private String gptComment;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
	private List<DiaryTarot> diaryTarots;

	private LocalDateTime deletedAt;

	@Nullable
	@Column(columnDefinition = "json")
	private String vector;

	public Diary() {
		super();
	}

	public void modifyDiaryDisclosure(DiaryOpenType diaryOpenType) {
		this.type = diaryOpenType;
	}

	public void addDiaryTarot(Tarot tarot, DiaryTarotType type) {
		DiaryTarot diaryTarot = new DiaryTarot(this, tarot, type);
		if (this.diaryTarots == null) {
			diaryTarots = new ArrayList<>();
		}
		this.diaryTarots.add(diaryTarot);
		diaryTarot.setDiary(this);
	}
}

