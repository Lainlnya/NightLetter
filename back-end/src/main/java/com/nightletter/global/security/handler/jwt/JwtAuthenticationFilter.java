package com.nightletter.global.security.handler.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = parseBearerToken(request);

			log.info("Token Info In JWT Filter : " + token);

			if (token == null) {
				filterChain.doFilter(request, response);
				return;
			}

			String memberId = jwtProvider.validate(token);

			log.info("MemberId Info In JWT Filter : " + memberId);

			if (memberId == null) {
				filterChain.doFilter(request, response);
				return;
			}

			Member member = memberRepository.findByMemberId(Integer.parseInt(memberId));
			//            Member member = memberRepository.findMemberByOAuth2Id(memberId);

			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));

			log.info("Called in Filter : Member info : " + member.toString());

			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			AbstractAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, authorities);

			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			securityContext.setAuthentication(authenticationToken);
			SecurityContextHolder.setContext(securityContext);

			log.info("Authentication Info In JWT Filter : " + SecurityContextHolder.getContext());

		} catch (Exception e) {
			log.info("ERROR OCCURED IN PARSING TOKEN");
			e.printStackTrace();
		}

		filterChain.doFilter(request, response);
	}

	private String parseBearerToken(HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();

	   // boolean hasAuthorization = false;
	   // String authorization = request.getHeader("Authorization");
	   // // Authorization 보유하고 있나?
	   // if (! hasAuthorization) return null;
	   // // Bearer 방식인가?
	   // boolean isBearer = authorization.startsWith("Bearer ");
	   // if (! isBearer) return null;
	   // String accessToken = authorization.substring(7);
	   String accessToken = null;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("access-token")) {
				accessToken = cookie.getValue();
				log.info("token type: ACCESS // token : " + accessToken);
			}
		}

		return accessToken;
	}
}
