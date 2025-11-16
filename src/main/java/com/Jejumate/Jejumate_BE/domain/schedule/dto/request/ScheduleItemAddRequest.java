package com.Jejumate.Jejumate_BE.domain.schedule.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ScheduleItemAddRequest {

    private Integer dayNumber;
    private String timeSlot;
    private String placeName;
    private String category;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private String description;
    private Integer orderIndex;
}
