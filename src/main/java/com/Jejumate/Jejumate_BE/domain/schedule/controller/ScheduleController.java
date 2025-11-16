package com.Jejumate.Jejumate_BE.domain.schedule.controller;

import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleCreateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleListResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "일정 생성",
            description = "챗봇으로부터 받은 여행 일정을 저장합니다. " +
                    "일정 제목, 날짜, 여행 스타일 등의 정보와 함께 " +
                    "Day별 장소 정보를 포함하여 저장합니다."
    )
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestHeader("user-id") Long userId,
            @RequestBody ScheduleCreateRequest request
    ) {
        ScheduleResponse response = scheduleService.createSchedule(userId, request);
        return ResponseEntity.ok(response);
    }

    // 일정 목록 조회
    @GetMapping
    @Operation(
            summary = "일정 목록 조회",
            description = "사용자가 저장한 모든 여행 일정 목록을 조회합니다. " +
                    "삭제된 일정은 제외하고 최신순으로 정렬됩니다. " +
                    "각 일정의 간단한 정보(제목, 날짜, 장소 개수, 카테고리)만 반환됩니다."
    )
    public ResponseEntity<List<ScheduleListResponse>> getSchedules(
            @RequestHeader("user-id") Long userId
    ) {
        List<ScheduleListResponse> schedules = scheduleService.getSchedules(userId);
        return ResponseEntity.ok(schedules);
    }

    // 일정 상세 조회
    @GetMapping("/{scheduleId}")
    @Operation(
            summary = "일정 상세 조회",
            description = "특정 일정의 상세 정보를 조회합니다. " +
                    "일정에 포함된 모든 장소(ScheduleItem) 정보가 " +
                    "Day별, 시간대별로 정렬되어 반환됩니다."
    )
    public ResponseEntity<ScheduleResponse> getScheduleDetail(
            @RequestHeader("user-id") Long userId,
            @PathVariable Long scheduleId
    ) {
        ScheduleResponse schedule = scheduleService.getScheduleDetail(userId, scheduleId);
        return ResponseEntity.ok(schedule);
    }

    // 일정 삭제
    @DeleteMapping("/{scheduleId}")
    @Operation(
            summary = "일정 삭제",
            description = "특정 일정을 삭제합니다 (Soft Delete). " +
                    "실제로 DB에서 삭제되지 않고 status가 DELETED로 변경됩니다. " +
                    "본인의 일정만 삭제할 수 있습니다."
    )
    public ResponseEntity<Void> deleteSchedule(
            @RequestHeader("user-id") Long userId,
            @PathVariable Long scheduleId
    ) {
        scheduleService.deleteSchedule(userId, scheduleId);
        return ResponseEntity.noContent().build();
    }

    // 일정 제목 수정
    @PatchMapping("/{scheduleId}/title")
    @Operation(
            summary = "일정 제목 수정",
            description = "저장된 일정의 제목을 수정합니다. " +
                    "본인의 일정만 수정할 수 있습니다."
    )
    public ResponseEntity<ScheduleResponse> updateTitle(
            @RequestHeader("user-id") Long userId,
            @PathVariable Long scheduleId,
            @RequestBody String newTitle
    ) {
        ScheduleResponse response = scheduleService.updateScheduleTitle(userId, scheduleId, newTitle);
        return ResponseEntity.ok(response);
    }
}
