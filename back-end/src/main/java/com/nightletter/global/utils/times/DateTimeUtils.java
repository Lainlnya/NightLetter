package com.nightletter.global.utils.times;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {

	public static LocalDateTime nowFromZone() {
		return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
	}

	public static Instant newFromZone() {
		return Instant.now().atZone(ZoneId.of("Asia/Seoul")).toInstant();
	}
}
