package com.Jejumate.Jejumate_BE.domain.chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ChatbotRequest {

    private String action;

    @JsonProperty("constraints")
    private ChatbotConstraintRequest constraints;
}