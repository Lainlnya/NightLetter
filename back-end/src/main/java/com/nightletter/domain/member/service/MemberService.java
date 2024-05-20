package com.nightletter.domain.member.service;

import com.nightletter.domain.member.dto.MemberNicknameResponse;

public interface MemberService {

	public MemberNicknameResponse getMemberNickname();
	public MemberNicknameResponse updateMemberNickname(String nickname);

	public void deleteMember();
}
