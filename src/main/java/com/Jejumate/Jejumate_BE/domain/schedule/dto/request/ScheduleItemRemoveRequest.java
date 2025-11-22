package com.Jejumate.Jejumate_BE.domain.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleItemRemoveRequest {

    private Integer dayNumber;
    private String timeSlot;
}
