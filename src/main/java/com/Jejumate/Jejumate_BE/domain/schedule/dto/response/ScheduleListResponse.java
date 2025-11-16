package com.Jejumate.Jejumate_BE.domain.schedule.dto.response;

import com.Jejumate.Jejumate_BE.domain.schedule.domain.Schedule;
import com.Jejumate.Jejumate_BE.domain.schedule.domain.ScheduleItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class ScheduleListResponse {
    private Long scheduleId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String companions;
    private Integer totalDays;
    private Integer totalPlaces;
    private List<String> categories;
    private LocalDateTime createdAt;

    public static ScheduleListResponse from(Schedule schedule) {
        Set<String> categorySet = schedule.getItems().stream()
                .map(ScheduleItem::getCategory)
                .collect(Collectors.toSet());

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
}
