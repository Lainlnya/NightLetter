package com.nightletter.domain.member.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nightletter.domain.member.dto.MemberNicknameResponse;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.global.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	public MemberNicknameResponse getMemberNickname() {
		Member member = getCurrentMember();

		return new MemberNicknameResponse(member.getNickname());
	}

	@Transactional
	@Override
	public MemberNicknameResponse updateMemberNickname(String nickname) {
		Member member = getCurrentMember();
		member.updateNickname(nickname);
		memberRepository.save(member);

		return new MemberNicknameResponse(member.getNickname());
	}

	@Override
	public void deleteMember() {
		Member member = getCurrentMember();
		memberRepository.delete(member);
	}

	private Member getCurrentMember() {
		return memberRepository.findByMemberId(getCurrentMemberId());
	}

	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String)authentication.getPrincipal());
	}

}
