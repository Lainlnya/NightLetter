package com.example.authservice.handler;


import com.example.authservice.db.entity.CustomOAuth2User;
import com.example.authservice.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String memberId = oAuth2User.getName();
        String token = jwtProvider.create(memberId);

        response.addHeader("Set-Cookie", accessCookie(token).toString());
        response.sendRedirect("http://localhost:3000/auth/oauth-response/");
    }

    private static ResponseCookie accessCookie(String token) {
        return ResponseCookie.from("access-token", token)
                .maxAge(Duration.of(30, ChronoUnit.MINUTES))
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
    }
}