package com.nightletter.domain.diary.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.global.common.BaseEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@AttributeOverride(name = "id", column = @Column(name = "diary_id"))
@SuperBuilder
public class Diary extends BaseEntity {
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", referencedColumnName = "member_id", updatable = false)
	private Member writer;
	private String content;
	@Enumerated(EnumType.STRING)
	private DiaryType type;
	private String gptComment;

	// todo. 벡터 추가.

	public Diary() {
		super();
	}

	public DiaryResponse toDto() {
		return DiaryResponse.builder()
			.id(this.writer.getId())
			.content(this.content)
			.type(this.type)
			.gptComment(this.gptComment)
			.build();
	}
}

