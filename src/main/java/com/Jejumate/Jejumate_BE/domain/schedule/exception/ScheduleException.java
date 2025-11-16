package com.Jejumate.Jejumate_BE.domain.schedule.exception;

import com.Jejumate.Jejumate_BE.global.exception.BaseErrorCode;
import lombok.Getter;

@Getter
public class ScheduleException extends RuntimeException {

    private final ScheduleErrorCode errorCode;

    public ScheduleException(ScheduleErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
