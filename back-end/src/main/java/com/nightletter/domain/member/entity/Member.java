package com.nightletter.domain.member.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.nightletter.domain.diary.entity.Scrap;
import com.nightletter.domain.social.entity.Chatroom;
import com.nightletter.global.common.BaseTimeEntity;

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
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Member SET deleted_at = now() WHERE member_id = ?")
@SQLRestriction("deleted_at IS NULL")
@Entity
public class Member extends BaseTimeEntity {
	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;

	private String OAuth2Id;

	private String email;

	private String nickname;

	private String profileImgUrl;

	@Enumerated(EnumType.STRING)
	private Provider provider;

	private LocalDateTime deletedAt;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	Set<Scrap> scraps = new HashSet<>();

	public void scrapDiary(Scrap scrap) {
		this.scraps.add(scrap);
		scrap.setMember(this);
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

}
