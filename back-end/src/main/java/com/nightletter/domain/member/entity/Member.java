package com.nightletter.domain.member.entity;

import com.nightletter.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

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
}
