package com.Jejumate.Jejumate_BE.domain.chatbot.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AiChatbotRequest {

    private List<Message> messages;

    @Getter
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}