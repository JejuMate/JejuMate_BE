package com.Jejumate.Jejumate_BE.domain.chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class ChatbotRequestDto {

    private String action;

    @JsonProperty("constraints")
    private ChatbotConstraintRequest constraints;
}