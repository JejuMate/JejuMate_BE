package com.Jejumate.Jejumate_BE.domain.schedule.converter;

import com.Jejumate.Jejumate_BE.domain.schedule.domain.Schedule;
import com.Jejumate.Jejumate_BE.domain.schedule.domain.ScheduleItem;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleCreateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleListResponse;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleConverter {

    private final ScheduleItemConverter itemConverter;

    public ScheduleResponse toDetailResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .totalDays(schedule.getTotalDays())
                .travelStyle(schedule.getTravelStyle())
                .companions(schedule.getCompanions())
                .ageGroup(schedule.getAgeGroup())
                .additionalRequest(schedule.getAdditionalRequest())
                .status(schedule.getStatus().name())
                .items(itemConverter.toDtoList(schedule.getItems()))
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    public ScheduleListResponse toListResponse(Schedule schedule) {
        // 카테고리 중복 제거 및 정렬
        Set<String> categorySet = schedule.getItems().stream()
                .map(ScheduleItem::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return ScheduleListResponse.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .companions(schedule.getCompanions())
                .totalDays(schedule.getTotalDays())
                .totalPlaces(schedule.getItems().size())
                .categories(new ArrayList<>(categorySet))
                .createdAt(schedule.getCreatedAt())
                .build();
    }

    public Schedule toEntity(Long userId, ScheduleCreateRequest request) {
        // 총 일수 계산
        int totalDays = calculateTotalDays(request.getStartDate(), request.getEndDate());

        // Schedule 생성
        Schedule schedule = Schedule.builder()
                .userId(userId)
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalDays(totalDays)
                .travelStyle(request.getTravelStyle())
                .companions(request.getCompanions())
                .ageGroup(request.getAgeGroup())
                .additionalRequest(request.getAdditionalRequest())
                .status(Schedule.ScheduleStatus.SAVED)
                .build();

        // ScheduleItem 추가
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            request.getItems().forEach(itemDto -> {
                ScheduleItem item = itemConverter.toEntity(itemDto);
                schedule.addItem(item);
            });
        }

        return schedule;
    }

    // List 변환
    public List<ScheduleListResponse> toListResponseList(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    // 총 일수 계산 헬퍼 메서드
    private int calculateTotalDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
