package com.nightletter.domain.member.service;

import java.util.Optional;

import com.nightletter.domain.member.dto.MemberNicknameResponse;

public interface MemberService {

	public MemberNicknameResponse getMemberNickname();
	public MemberNicknameResponse updateMemberNickname(String nickname);

}
