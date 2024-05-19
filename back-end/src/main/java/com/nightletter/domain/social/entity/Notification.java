package com.nightletter.domain.social.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.member.entity.Member;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE Notification SET deleted_at = now() WHERE notification_id = ?")
@SQLRestriction("deleted_at IS NULL")
@Entity
public class Notification {
	@Id @Column(name = "notification_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "notification_type")
	private NotificationType type;

	@Column(name = "is_read")
	boolean isRead;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "read_at")
	private LocalDateTime readAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public void readNotification() {
		isRead = true;
	}
}
