package com.nightletter.global.utils.times;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class DateTimeUtils {

	public static LocalDateTime nowFromZone() {
		return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
	}

	public static Instant newFromZone() {
		return DateTimeUtils.nowFromZone().atZone(ZoneId.of("Asia/Seoul")).toInstant();
	}
}
