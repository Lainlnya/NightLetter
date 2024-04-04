package com.nightletter.global.utils.times;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class DateTimeUtils {

	public static LocalDateTime nowFromZone() {
		return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
	}

	public static Instant tokenExpireTime() {
		return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("+09:00"));
	}
}
