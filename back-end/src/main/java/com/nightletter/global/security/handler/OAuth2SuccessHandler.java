package com.nightletter.global.security.handler;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nightletter.domain.member.entity.CustomOAuth2User;
import com.nightletter.global.security.handler.jwt.JwtProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;

	private static ResponseCookie accessCookie(String token) {
		return ResponseCookie.from("access-token", token)
			.maxAge(Duration.of(30, ChronoUnit.MINUTES))
			.httpOnly(true)
			.path("/")
			.sameSite("None")
			.secure(true)
			.build();
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		String memberId = oAuth2User.getName();
		String token = jwtProvider.create(memberId);

		response.addHeader("Set-Cookie", accessCookie(token).toString());
		response.sendRedirect("http://localhost:3000/auth/oauth-response/");
	}
}