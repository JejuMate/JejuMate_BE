package com.Jejumate.Jejumate_BE.domain.schedule.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleItemRemoveRequest {

    private Integer dayNumber;
    private String timeSlot;
}
