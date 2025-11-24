package com.Jejumate.Jejumate_BE.domain.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleItemUpdateRequest {

    private Integer targetDayNumber;
    private String targetTimeSlot;

    private String placeName;
    private String category;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private String description;
    private Integer orderIndex;

    private Integer newDayNumber;
    private String newTimeSlot;
}
