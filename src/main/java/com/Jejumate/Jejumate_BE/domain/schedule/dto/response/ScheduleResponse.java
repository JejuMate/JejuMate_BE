package com.Jejumate.Jejumate_BE.domain.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private String travelStyle;
    private String companions;
    private String ageGroup;
    private String additionalRequest;
    private String status;
    private List<ScheduleItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
