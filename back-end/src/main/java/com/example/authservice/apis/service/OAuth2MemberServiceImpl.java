package com.example.authservice.apis.service;

import com.example.authservice.db.entity.CustomOAuth2User;
import com.example.authservice.db.entity.Member;
import com.example.authservice.db.entity.Provider;
import com.example.authservice.db.entity.Role;
import com.example.authservice.db.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.authservice.db.entity.Provider.KAKAO;
import static com.example.authservice.db.entity.Role.ROLE_MEMBER;

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
            System.out.println("======================================OAUTH2 USER INFO======================================");
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Member member = null;
        String OAuth2Id = null;
        String email = null;
        String nickname = null;
        String profileImgUrl = null;
        Provider type = null;
        Role role = null;

        switch(oauthClientName) {
            case "kakao":
                OAuth2Id = "kakao_" + oAuth2User.getAttribute("id");
                
                member = memberRepository.findMemberByOAuth2Id(OAuth2Id);
                
                // 멤버가 존재하면 해당 멤버 정보 반환
                if (member != null) {
                    return new CustomOAuth2User(Integer.toString(member.getMemberId()));
                }

                Map<String, String> kakaoAccountInfo = oAuth2User.getAttribute("kakao_account");
                Map<String, String> kakaoProfileInfo = oAuth2User.getAttribute("properties");
                email = kakaoAccountInfo.get("email");
                nickname = kakaoProfileInfo.get("nickname");
                profileImgUrl = kakaoProfileInfo.get("profile_image");
                type = KAKAO;
                role = ROLE_MEMBER;

                break;
            case "apple":
                break;
        }

        // 에러 처리
        if (type == null) {
            return null;
        }

        member = Member.builder()
                .OAuth2Id(OAuth2Id)
                .email(email)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .type(type)
                .role(role)
                .build();

        log.info(member.toString());

        // 존재 확인 후 save
        member = memberRepository.save(member);

        return new CustomOAuth2User(Integer.toString(member.getMemberId()));
    }
}
