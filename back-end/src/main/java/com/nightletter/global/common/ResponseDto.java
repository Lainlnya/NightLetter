package com.nightletter.global.common;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseDto {
	String code;
	String message;

	public static ResponseEntity<ResponseDto> databaseError() {
		return ResponseEntity.internalServerError().body(
			ResponseDto.builder()
				.code("DBE")
				.message("Database error.")
				.build()
		);
	}

	public static ResponseEntity<ResponseDto> validationFail() {
		return ResponseEntity.internalServerError().body(
			ResponseDto.builder()
				.code("VE")
				.message("Validation failed.")
				.build()
		);
	}
}
