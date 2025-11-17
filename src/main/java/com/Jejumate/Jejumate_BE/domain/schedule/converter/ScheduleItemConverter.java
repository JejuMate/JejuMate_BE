package com.Jejumate.Jejumate_BE.domain.schedule.converter;

import com.Jejumate.Jejumate_BE.domain.schedule.domain.ScheduleItem;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleItemAddRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleItemUpdateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleItemDto;
import com.Jejumate.Jejumate_BE.domain.schedule.enums.TimeSlot;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleItemConverter {

    public ScheduleItemDto toDto(ScheduleItem item) {
        if (item == null) {
            return null;
        }

        return ScheduleItemDto.builder()
                .id(item.getId())
                .dayNumber(item.getDayNumber())
                .timeSlot(item.getTimeSlot().name())
                .placeName(item.getPlaceName())
                .category(item.getCategory())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .address(item.getAddress())
                .description(item.getDescription())
                .orderIndex(item.getOrderIndex())
                .build();
    }

    public ScheduleItem toEntity(ScheduleItemDto dto) {
        if (dto == null) {
            return null;
        }

        TimeSlot timeSlot = safeConvertToTimeSlot(dto.getTimeSlot());

        return ScheduleItem.builder()
                .dayNumber(dto.getDayNumber())
                .timeSlot(timeSlot)
                .placeName(dto.getPlaceName())
                .category(dto.getCategory())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .orderIndex(dto.getOrderIndex())
                .build();
    }

    public ScheduleItem toEntity(ScheduleItemAddRequest dto) {
        if (dto == null) {
            return null;
        }

        TimeSlot timeSlot = safeConvertToTimeSlot(dto.getTimeSlot());

        return ScheduleItem.builder()
                .dayNumber(dto.getDayNumber())
                .timeSlot(timeSlot)
                .placeName(dto.getPlaceName())
                .category(dto.getCategory())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .orderIndex(dto.getOrderIndex())
                .build();
    }

    public ScheduleItem toEntity(ScheduleItemUpdateRequest dto) {
        if (dto == null) {
            return null;
        }

        TimeSlot timeSlot = safeConvertToTimeSlot(dto.getNewTimeSlot());

        return ScheduleItem.builder()
                .dayNumber(dto.getNewDayNumber())
                .timeSlot(timeSlot)
                .placeName(dto.getPlaceName())
                .category(dto.getCategory())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .orderIndex(dto.getOrderIndex())
                .build();
    }

    // List 변환
    public List<ScheduleItemDto> toDtoList(List<ScheduleItem> items) {
        if (items == null) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TimeSlot safeConvertToTimeSlot(String timeSlotStr) {
        if (timeSlotStr == null || timeSlotStr.trim().isEmpty()) {
            throw new IllegalArgumentException("TimeSlot은 null이거나 비어있을 수 없습니다.");
        }
        try {
            return TimeSlot.valueOf(timeSlotStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 TimeSlot 값입니다: " + timeSlotStr);
        }
    }
}
