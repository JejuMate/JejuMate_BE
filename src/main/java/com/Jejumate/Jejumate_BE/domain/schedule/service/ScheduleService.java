package com.Jejumate.Jejumate_BE.domain.schedule.service;

import com.Jejumate.Jejumate_BE.domain.schedule.converter.ScheduleConverter;
import com.Jejumate.Jejumate_BE.domain.schedule.domain.Schedule;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleCreateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleListResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleResponse;
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
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));
        return scheduleConverter.toDetailResponse(schedule);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository
                .findScheduleDetailById(scheduleId, userId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        schedule.updateStatus(Schedule.ScheduleStatus.DELETED);
    }

    // 일정 제목 수정
    @Transactional
    public ScheduleResponse updateScheduleTitle(Long userId, Long scheduleId, String newTitle) {
        Schedule schedule = scheduleRepository
                .findScheduleDetailById(scheduleId, userId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));
        schedule.updateTitle(newTitle);
        return scheduleConverter.toDetailResponse(schedule);
    }
}
