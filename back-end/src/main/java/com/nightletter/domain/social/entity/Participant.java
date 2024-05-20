package com.nightletter.domain.social.entity;

import java.time.LocalDateTime;
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
public class Participant {
	@Id @Column(name = "room_participant_id")
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatroom;

	@Column
	private LocalDateTime participatedTime;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;

		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}

		Participant participant = (Participant) obj;

		return participant.getChatroom().id.equals(this.chatroom.id)
			&& participant.getMember().getMemberId().equals(this.getMember().getMemberId()) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(member, chatroom);
	}
}
