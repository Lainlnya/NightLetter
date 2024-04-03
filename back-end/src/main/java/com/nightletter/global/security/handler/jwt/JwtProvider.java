package com.nightletter.global.security.handler.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nightletter.global.utils.times.DateTimeUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	@Value("${jwt.secret-key}")
	private String secretKey;

	public String create(String memberId) {

		Date expiredDate = Date.from(DateTimeUtils.newFromZone().plus(30, ChronoUnit.MINUTES));
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes((StandardCharsets.UTF_8)));

		return Jwts.builder()
			.signWith(key, SignatureAlgorithm.HS256)
			.setSubject(memberId)
			.setIssuedAt(new Date())
			.setExpiration(expiredDate)
			.compact();
	}

	public String validate(String jwt) {

		String subject = null;
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

		try {
			subject = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		System.out.println(subject);

		return subject;
	}
}
