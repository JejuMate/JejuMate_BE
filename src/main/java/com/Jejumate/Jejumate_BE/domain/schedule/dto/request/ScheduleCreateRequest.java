package com.Jejumate.Jejumate_BE.domain.schedule.dto.request;

import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String travelStyle;
    private String companions;
    private String ageGroup;
    private String additionalRequest;
    private List<ScheduleItemDto> items;
}

