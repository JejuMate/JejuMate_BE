package com.Jejumate.Jejumate_BE.domain.chatbot.dto.request;

import com.Jejumate.Jejumate_BE.domain.chatbot.enums.ChatbotAction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ChatbotResponse {

    @JsonProperty("response_text")
    private String responseText;

    private ChatbotAction action;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("flight_info")
    private String flightInfo;

    //생성된 일정 (create_schedule)
    private List<ChatbotScheduleItem> schedule;

    //특정 일정 (수정, 삭제)
    private ChatbotTarget target;

    //대안 장소 목록 (suggest_alternative)
    @JsonProperty("alternative_places")
    private List<ChatbotPlace> alternativePlaces;

    //수정된 장소 (update_schedule)
    @JsonProperty("new_place")
    private ChatbotPlace newPlace;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class ChatbotScheduleItem {
        private int day;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate date;

        @JsonProperty("time_slot")
        private String timeSlot;

        private ChatbotPlace place;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class ChatbotTarget {
        private int day;

        @JsonProperty("time_slot")
        private String timeSlot;

        private String place;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class ChatbotPlace {
        private String name;
        private String category;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String address;
        private String description;
    }
}