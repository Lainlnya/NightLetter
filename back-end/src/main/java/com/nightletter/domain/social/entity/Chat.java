package com.nightletter.domain.social.entity;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import com.nightletter.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Chat {

	@Id @Column(name = "chat_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false)
	Member sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chatroom_id", updatable = false)
	Chatroom chatroom;

	@Column(name = "send_time")
	LocalDateTime sendTime;

	@Column(name = "message")
	String message;
}
