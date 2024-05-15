package com.nightletter.domain.social.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.entity.Tarot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Chatroom {

	@Id
	@Column(name = "chatroom_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@OneToOne
	@JoinColumn(name = "tarot_id")
	Tarot tarot;

	// TODO room Number 필요.

	@OneToMany
	@JoinColumn(name = "member_id")
	List<Member> participants;

}
