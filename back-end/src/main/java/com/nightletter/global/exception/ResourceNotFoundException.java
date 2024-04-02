package com.nightletter.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
	private final ErrorCode errorCode;
	private final String detailMessage;
}
