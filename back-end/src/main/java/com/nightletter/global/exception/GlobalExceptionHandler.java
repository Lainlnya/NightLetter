package com.nightletter.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nightletter.global.common.ResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception) {
		return ResponseDto.validationFail();
	}

	@ExceptionHandler(RecsysConnectionException.class)
	public ResponseEntity<?> recsysConnectionException(RecsysConnectionException e){
		ErrorCode errorCode = CommonErrorCode.RECOMMEND_SYS_CONNECTION_ERROR;
		return handleExceptionInternal(errorCode);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException e){
		ErrorCode errorCode = CommonErrorCode.RESOURCE_NOT_FOUND;
		return handleExceptionInternal(errorCode, e.getDetailMessage());
	}

	private ResponseEntity<?> handleExceptionInternal(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode));
	}

	private ResponseEntity<?> handleExceptionInternal(ErrorCode errorCode, String message) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode, message));
	}

	private ResponseDto makeErrorResponse(ErrorCode errorCode) {
		return ResponseDto.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.build();
	}

	private ResponseDto makeErrorResponse(ErrorCode errorCode, String message) {
		return ResponseDto.builder()
			.code(errorCode.name())
			.message(message)
			.build();
	}
}
