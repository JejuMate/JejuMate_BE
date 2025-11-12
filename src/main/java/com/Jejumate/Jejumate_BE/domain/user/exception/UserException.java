package com.Jejumate.Jejumate_BE.domain.user.exception;

import com.Jejumate.Jejumate_BE.global.exception.BaseErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public UserException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}