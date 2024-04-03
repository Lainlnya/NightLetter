package com.nightletter.domain.member.service;

import static com.nightletter.domain.member.entity.Provider.*;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nightletter.domain.member.entity.CustomOAuth2User;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.entity.Provider;
import com.nightletter.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2MemberServiceImpl extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		String oauthClientName = userRequest.getClientRegistration().getClientName();

		try {
			System.out.println(
				"======================================OAUTH2 USER INFO======================================");
			System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Member member = null;
		String OAuth2Id = null;
		String email = null;
		String nickname = null;
		String profileImgUrl = null;
		Provider provider = null;

		switch (oauthClientName) {
			case "kakao":
				OAuth2Id = "kakao_" + oAuth2User.getAttribute("id");

				member = memberRepository.findMemberByOAuth2Id(OAuth2Id);

				// 멤버가 존재하면 해당 멤버 정보 반환
				if (member != null) {
					return new CustomOAuth2User(Long.toString(member.getMemberId()));
				}

				Map<String, String> kakaoAccountInfo = oAuth2User.getAttribute("kakao_account");
				Map<String, String> kakaoProfileInfo = oAuth2User.getAttribute("properties");
				email = kakaoAccountInfo.get("email");
				nickname = kakaoProfileInfo.get("nickname");
				profileImgUrl = kakaoProfileInfo.get("profile_image");
				provider = KAKAO;

				break;
			case "apple":
				break;
		}

		// 에러 처리
		if (provider == null) {
			return null;
		}

		member = Member.builder()
			.OAuth2Id(OAuth2Id)
			.email(email)
			.nickname(nickname)
			.profileImgUrl(profileImgUrl)
			.provider(provider)
			.build();

		log.info(member.toString());

		// 존재 확인 후 save
		member = memberRepository.save(member);

		return new CustomOAuth2User(Long.toString(member.getMemberId()));
	}
}
