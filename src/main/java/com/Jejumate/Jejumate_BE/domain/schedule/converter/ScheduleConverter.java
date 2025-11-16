package com.Jejumate.Jejumate_BE.domain.schedule.converter;

import com.Jejumate.Jejumate_BE.domain.schedule.domain.Schedule;
import com.Jejumate.Jejumate_BE.domain.schedule.domain.ScheduleItem;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleCreateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleItemDto;
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

        String title = generateTitle(request);
        // 총 일수 계산
        int totalDays = calculateTotalDays(request.getStartDate(), request.getEndDate());

        // Schedule 생성
        Schedule schedule = Schedule.builder()
                .userId(userId)
                .title(title)
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

    private String generateTitle(ScheduleCreateRequest request) {
        // 2. 장소명 기반 제목 생성
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            return generateTitleFromPlaces(request.getItems());
        }

        // 3. 여행 스타일 기반 제목 생성 (Fallback)
        return generateTitleFromConstraints(request);
    }

    private String generateTitleFromPlaces(List<ScheduleItemDto> items) {
        String firstPlace = items.get(0).getPlaceName();

        // 2개 이상이면 "&"로 연결
        if (items.size() > 1) {
            String secondPlace = items.get(1).getPlaceName();
            return firstPlace + " & " + secondPlace + " 여행";
        }

        // 1개만 있으면
        return firstPlace + " 여행";
    }

    private String generateTitleFromConstraints(ScheduleCreateRequest request) {
        String companions = getCompanionsText(request.getCompanions());
        String style = request.getTravelStyle() != null ?
                request.getTravelStyle() : "제주도";

        return companions + " " + style + " 여행";
    }

    private String getCompanionsText(String companions) {
        if (companions == null) {
            return "나만의";
        }

        switch (companions) {
            case "가족":
                return "가족과 함께하는";
            case "친구":
                return "친구와 함께하는";
            case "연인":
                return "연인과 함께하는";
            case "혼자":
                return "나만의";
            default:
                return companions + "과 함께하는";
        }
    }

    // 총 일수 계산 헬퍼 메서드
    private int calculateTotalDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
