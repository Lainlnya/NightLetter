package com.example.authservice.db.response;

import com.example.authservice.common.ResponseCode;
import com.example.authservice.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
