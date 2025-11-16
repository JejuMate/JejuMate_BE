package com.Jejumate.Jejumate_BE.domain.schedule.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode {

    /**
     * 1xx: 클라이언트가 수정해야 할 입력값 문제
     * 2xx: 서버에서 리소스를 찾을 수 없는 문제
     * 3xx: 권한/인증 문제
     * 4xx: 비즈니스 로직 위반
     */

    INVALID_TIMESLOT(HttpStatus.BAD_REQUEST, "SCHEDULE_101", "유효하지 않은 TimeSlot 값입니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "SCHEDULE_102", "종료 날짜가 시작 날짜보다 빠를 수 없습니다."),
    DAY_NUMBER_OUT_OF_BOUNDS(HttpStatus.BAD_REQUEST, "SCHEDULE_103", "일정의 총 일수 범위를 벗어난 Day입니다."),

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_201", "일정을 찾을 수 없습니다."),
    SCHEDULE_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_202", "해당 일정 아이템을 찾을 수 없습니다."),

    NO_SCHEDULE_AUTHORITY(HttpStatus.FORBIDDEN, "SCHEDULE_301", "해당 일정에 대한 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
