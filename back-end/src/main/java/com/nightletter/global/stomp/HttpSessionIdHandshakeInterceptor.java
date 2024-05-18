package com.nightletter.global.stomp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.nightletter.global.security.handler.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {

	private final JwtProvider jwtProvider;

	@Value("${spring.security.dev-token}")
	private String devToken;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Map<String, Object> attributes) throws Exception {

		String token = devToken;
		// String token = parseBearerToken(request);

		String memberId = jwtProvider.validate(token); // JWT 토큰 검증 및 사용자 ID 추출

		if (memberId != null) {

			System.out.println("WEBSOCKET MEMBER_ID : " + memberId);

			attributes.put("memberId", memberId);

			return true;
		}

		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Exception exception) {

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
