package com.Jejumate.Jejumate_BE.domain.user.exception;

import com.Jejumate.Jejumate_BE.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    /**
     * 1xx: 클라이언트가 수정해야 할 입력값 문제
     * 2xx: 서버에서 리소스를 찾을 수 없는 문제
     * 3xx: 권한/인증 문제
     * 4xx: 비즈니스 로직 위반
     */

    //jwt
    NICKNAME_IS_NULL(HttpStatus.BAD_REQUEST, "USER_AUTH_301", "닉네임이 존재하지 않습니다."), // (JwtTokenProvider에서 닉네임을 사용할 경우)
    JWT_IS_NULL(HttpStatus.UNAUTHORIZED, "USER_AUTH_302", "JWT 토큰이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "USER_AUTH_303", "유효하지 않은 토큰입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_AUTH_304", "사용자를 찾을 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}