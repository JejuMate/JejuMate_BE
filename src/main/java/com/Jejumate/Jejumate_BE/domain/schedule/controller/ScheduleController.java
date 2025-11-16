package com.Jejumate.Jejumate_BE.domain.schedule.controller;

import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleCreateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleListResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestHeader("user-id") Long userId,
            @RequestBody ScheduleCreateRequest request
    ) {
        ScheduleResponse response = scheduleService.createSchedule(userId, request);
        return ResponseEntity.ok(response);
    }

    // 일정 목록 조회
    @GetMapping
    public ResponseEntity<List<ScheduleListResponse>> getSchedules(
            @RequestHeader("user-id") Long userId
    ) {
        List<ScheduleListResponse> schedules = scheduleService.getSchedules(userId);
        return ResponseEntity.ok(schedules);
    }

    // 일정 상세 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getScheduleDetail(
            @RequestHeader("user-id") Long userId,
            @PathVariable Long scheduleId
    ) {
        ScheduleResponse schedule = scheduleService.getScheduleDetail(userId, scheduleId);
        return ResponseEntity.ok(schedule);
    }

    // 일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @RequestHeader("user-id") Long userId,
            @PathVariable Long scheduleId
    ) {
        scheduleService.deleteSchedule(userId, scheduleId);
        return ResponseEntity.noContent().build();
    }

    // 일정 제목 수정
    @PatchMapping("/{scheduleId}/title")
    public ResponseEntity<ScheduleResponse> updateTitle(
            @RequestHeader("user-id") Long userId,
            @PathVariable Long scheduleId,
            @RequestBody String newTitle
    ) {
        ScheduleResponse response = scheduleService.updateScheduleTitle(userId, scheduleId, newTitle);
        return ResponseEntity.ok(response);
    }
}
