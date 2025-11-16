package com.Jejumate.Jejumate_BE.domain.schedule.service;

import com.Jejumate.Jejumate_BE.domain.schedule.converter.ScheduleConverter;
import com.Jejumate.Jejumate_BE.domain.schedule.converter.ScheduleItemConverter;
import com.Jejumate.Jejumate_BE.domain.schedule.domain.Schedule;
import com.Jejumate.Jejumate_BE.domain.schedule.domain.ScheduleItem;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleCreateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleItemAddRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleItemRemoveRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleItemUpdateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleListResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.enums.ScheduleStatus;
import com.Jejumate.Jejumate_BE.domain.schedule.enums.TimeSlot;
import com.Jejumate.Jejumate_BE.domain.schedule.exception.ScheduleErrorCode;
import com.Jejumate.Jejumate_BE.domain.schedule.exception.ScheduleException;
import com.Jejumate.Jejumate_BE.domain.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleConverter scheduleConverter;
    private final ScheduleItemConverter scheduleItemConverter;

    // 일정 생성
    @Transactional
    public ScheduleResponse createSchedule(Long userId, ScheduleCreateRequest request) {
        Schedule schedule = scheduleConverter.toEntity(userId, request);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleConverter.toDetailResponse(savedSchedule);
    }

    // 일정 목록 조회
    public List<ScheduleListResponse> getSchedules(Long userId) {
        List<Schedule> schedules = scheduleRepository.findSchedulesByUserId(userId);
        return scheduleConverter.toListResponseList(schedules);
    }

    // 일정 상세 조회
    public ScheduleResponse getScheduleDetail(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository
                .findScheduleDetailById(scheduleId, userId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        return scheduleConverter.toDetailResponse(schedule);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository
                .findScheduleDetailById(scheduleId, userId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        schedule.updateStatus(ScheduleStatus.DELETED);
    }

    // 일정 제목 수정
    @Transactional
    public ScheduleResponse updateScheduleTitle(Long userId, Long scheduleId, String newTitle) {
        Schedule schedule = scheduleRepository
                .findScheduleDetailById(scheduleId, userId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        schedule.updateTitle(newTitle);
        return scheduleConverter.toDetailResponse(schedule);
    }

    @Transactional
    public ScheduleResponse addScheduleItem(Long userId, Long scheduleId, ScheduleItemAddRequest request) {
        Schedule schedule = findScheduleForUser(userId, scheduleId);

        ScheduleItem newItem = scheduleItemConverter.toEntity(request);
        schedule.addItem(newItem);

        return scheduleConverter.toDetailResponse(schedule);
    }


    // 일정 아이템(장소) 교체 (Update)
    @Transactional
    public ScheduleResponse updateScheduleItem(Long userId, Long scheduleId, ScheduleItemUpdateRequest request) {
        Schedule schedule = findScheduleForUser(userId, scheduleId);

        TimeSlot targetTimeSlot = convertToTimeSlot(request.getTargetTimeSlot());
        ScheduleItem itemToReplace = schedule
                .findItemByDayAndTime(request.getTargetDayNumber(), targetTimeSlot)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_ITEM_NOT_FOUND));

        ScheduleItem newItem = scheduleItemConverter.toEntity(request);
        schedule.removeItem(itemToReplace);
        schedule.addItem(newItem);

        return scheduleConverter.toDetailResponse(schedule);
    }

    // 일정 아이템(장소) 삭제
    @Transactional
    public ScheduleResponse removeScheduleItem(Long userId, Long scheduleId, ScheduleItemRemoveRequest request) {
        Schedule schedule = findScheduleForUser(userId, scheduleId);

        TimeSlot timeSlot = convertToTimeSlot(request.getTimeSlot());
        ScheduleItem itemToRemove = schedule
                .findItemByDayAndTime(request.getDayNumber(), timeSlot)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_ITEM_NOT_FOUND));

        schedule.removeItem(itemToRemove);

        return scheduleConverter.toDetailResponse(schedule);
    }


    // 사용자 일정 조회
    private Schedule findScheduleForUser(Long userId, Long scheduleId) {
        return scheduleRepository
                .findScheduleDetailById(scheduleId, userId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    // TimeSlot 문자열 변환 (Service 내부용)
    private TimeSlot convertToTimeSlot(String timeSlotStr) {
        try {
            return TimeSlot.valueOf(timeSlotStr);
        } catch (Exception e) {
            throw new ScheduleException(ScheduleErrorCode.INVALID_TIMESLOT);
        }
    }
}
