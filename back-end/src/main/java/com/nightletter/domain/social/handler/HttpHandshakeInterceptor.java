package com.nightletter.domain.social.handler;

import java.util.List;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.global.security.handler.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

		if (request instanceof ServletServerHttpRequest) {

			HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

			String token = parseBearerToken(servletRequest);

			if (token == null) {
				return false;
			}

			String memberId = jwtProvider.validate(token);

			Member member = memberRepository.findByMemberId(Integer.parseInt(memberId));

			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));

			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			AbstractAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, authorities);

			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(servletRequest));

			securityContext.setAuthentication(authenticationToken);
			SecurityContextHolder.setContext(securityContext);
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Exception exception) {
	}

	private String parseBearerToken(HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();

		String accessToken = null;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("access-token")) {
				accessToken = cookie.getValue();
			}
		}

		return accessToken;
	}
}
