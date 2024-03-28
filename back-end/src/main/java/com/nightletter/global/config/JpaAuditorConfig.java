package com.nightletter.global.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JpaAuditorConfig implements AuditorAware<Integer> {

//	private final MemberRepository memberRepository;

	@Override
	public Optional<Integer> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		System.out.println(authentication.getPrincipal());

		// 자체 작성한 find 쿼리 말고 원래 있던 기능 쓰니까 됨.
		// 아마 자체 작성하는 find 는 flush 되는 듯 ? 추후 알아봐야 함.
		Optional<Member> member = memberRepository.findById(Integer.parseInt((String)authentication.getPrincipal()));

//		Optional<Member> member = memberRepository.findById(Integer.parseInt((String) authentication.getPrincipal()));

		return Optional.of(Integer.parseInt((String) authentication.getPrincipal()));
	}
}
