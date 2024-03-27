package com.nightletter.domain.member.entity;

import com.nightletter.global.common.BaseEntity;

import com.nightletter.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
