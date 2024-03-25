package com.nightletter.apis.config;

import com.nightletter.db.entity.Member;
import com.nightletter.db.repository.MemberRepository;
import com.nightletter.db.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class JpaAuditorConfig implements AuditorAware<Member> {

    private final MemberRepository memberRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        System.out.println(authentication.getPrincipal());

        // 자체 작성한 find 쿼리 말고 원래 있던 기능 쓰니까 됨.
        // 아마 자체 작성하는 find 는 flush 되는 듯 ? 추후 알아봐야 함.
        Optional<Member> member = memberRepository.findById(Integer.parseInt((String) authentication.getPrincipal()));

        return member;
    }
}
