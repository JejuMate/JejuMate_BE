package com.Jejumate.Jejumate_BE.domain.chatbot.service;

import com.Jejumate.Jejumate_BE.domain.chatbot.client.ChatbotClient;
import com.Jejumate.Jejumate_BE.domain.chatbot.dto.request.ChatbotResponse;
import com.Jejumate.Jejumate_BE.domain.chatbot.enums.ChatbotAction;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleCreateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleItemRemoveRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.request.ScheduleItemUpdateRequest;
import com.Jejumate.Jejumate_BE.domain.schedule.dto.response.ScheduleItemDto;
import com.Jejumate.Jejumate_BE.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final ChatbotClient chatbotClient;
    private final ScheduleService scheduleService;

    @Transactional
    public ChatbotResponse chat(Long userId, String message, Long currentScheduleId) {

        //챗봇과 통신
        ChatbotResponse response = chatbotClient.sendChat(message);
        ChatbotAction action = response.getAction();

        //Action에 따른 ScheduleService 호출
        if (action != null) {
            switch (action) {
                case CREATE_SCHEDULE -> handleCreateSchedule(userId, response);
                case UPDATE_SCHEDULE -> handleUpdateSchedule(userId, currentScheduleId, response);
                case REMOVE_PLACE -> handleRemovePlace(userId, currentScheduleId, response);
            }
        }

        return response;
    }

    //핸들러 메서드 (일정 생성)
    private void handleCreateSchedule(Long userId, ChatbotResponse response) {
        if (response.getSchedule() == null || response.getSchedule().isEmpty()) {
            return;
        }

        LocalDate startDate = response.getStartDate();
        LocalDate endDate = response.getEndDate();

        //일정 아이템으로 변환
        List<ScheduleItemDto> items = response.getSchedule().stream()
                .map(item -> ScheduleItemDto.builder()
                        .dayNumber(item.getDay())
                        .timeSlot(item.getTimeSlot())
                        .placeName(item.getPlace().getName())
                        .category(item.getPlace().getCategory())
                        .latitude(item.getPlace().getLatitude())
                        .longitude(item.getPlace().getLongitude())
                        .address(item.getPlace().getAddress())
                        .description(item.getPlace().getDescription())
                        .build())
                .collect(Collectors.toList());

        //일정 생성 요청 객체 생성
        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .items(items)
                .build();

        //일정 생성
        scheduleService.createSchedule(userId, createRequest);
    }

    //핸들러 메서드 (일정 아이템 수정)
    private void handleUpdateSchedule(Long userId, Long scheduleId, ChatbotResponse response) {
        if (scheduleId == null || response.getTarget() == null || response.getNewPlace() == null) {
            return;
        }

        ChatbotResponse.ChatbotTarget target = response.getTarget();
        ChatbotResponse.ChatbotPlace newPlace = response.getNewPlace();

        ScheduleItemUpdateRequest updateRequest = new ScheduleItemUpdateRequest(
                target.getDay(),
                target.getTimeSlot(),
                newPlace.getName(),
                newPlace.getCategory(),
                newPlace.getLatitude(),
                newPlace.getLongitude(),
                newPlace.getAddress(),
                newPlace.getDescription(),
                0,
                target.getDay(),
                target.getTimeSlot()
        );

        scheduleService.updateScheduleItem(userId, scheduleId, updateRequest);
    }

    //핸들러 메서드 (일정 아이템 삭제)
    private void handleRemovePlace(Long userId, Long scheduleId, ChatbotResponse response) {
        if (scheduleId == null || response.getTarget() == null) return;

        ChatbotResponse.ChatbotTarget target = response.getTarget();

        ScheduleItemRemoveRequest removeRequest = new ScheduleItemRemoveRequest(
                target.getDay(),
                target.getTimeSlot()
        );

        scheduleService.removeScheduleItem(userId, scheduleId, removeRequest);
    }
}