package com.nightletter.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "INVALID PARAMETER INCLUDED"),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE NOT EXIST"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),
	RECOMMEND_SYS_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RECOMMEND SYS CONNECTION ERROR");

	private final HttpStatus httpStatus;
	private final String message;
}
