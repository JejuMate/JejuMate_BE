package com.Jejumate.Jejumate_BE.domain.chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@ToString
public class ChatbotConstraintRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("travel_style")
    private String travelStyle;

    private String companions;

    @JsonProperty("age_group")
    private String ageGroup;

    @JsonProperty("additional_request")
    private String additionalRequest;
}