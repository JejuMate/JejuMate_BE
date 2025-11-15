package com.Jejumate.Jejumate_BE.domain.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleItemDto {

    private Long id;
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
