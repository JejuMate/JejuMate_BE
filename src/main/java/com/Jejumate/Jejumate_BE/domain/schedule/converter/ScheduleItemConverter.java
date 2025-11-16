package com.Jejumate.Jejumate_BE.domain.schedule.converter;

import com.Jejumate.Jejumate_BE.domain.schedule.domain.ScheduleItem;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleItemDto;
import com.Jejumate.Jejumate_BE.domain.schedule.enums.TimeSlot;  // ✅ 임포트 추가!
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

        return ScheduleItem.builder()
                .dayNumber(dto.getDayNumber())
                .timeSlot(TimeSlot.valueOf(dto.getTimeSlot()))  // ✅ 수정!
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
}
