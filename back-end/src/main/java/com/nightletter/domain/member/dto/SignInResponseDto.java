package com.nightletter.domain.member.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nightletter.global.common.ResponseCode;
import com.nightletter.global.common.ResponseDto;
import com.nightletter.global.common.ResponseMessage;

import lombok.Getter;

@Getter
public class SignInResponseDto {
	private String token;
	private int expirationTime;

	private SignInResponseDto(String token) {
		super();
		this.token = token;
		expirationTime = 3600;
	}

	public static ResponseEntity<SignInResponseDto> success(String token) {
		SignInResponseDto responseBody = new SignInResponseDto(token);
		return ResponseEntity.ok(responseBody);
	}

	public static ResponseEntity<ResponseDto> signInFail() {
		ResponseDto responseDto = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
	}
}
