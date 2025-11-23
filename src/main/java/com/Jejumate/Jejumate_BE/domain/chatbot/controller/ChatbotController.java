package com.Jejumate.Jejumate_BE.domain.chatbot.controller;

import com.Jejumate.Jejumate_BE.domain.chatbot.dto.request.ChatbotResponse;
import com.Jejumate.Jejumate_BE.domain.chatbot.dto.response.ChatbotConstraintRequest;
import com.Jejumate.Jejumate_BE.domain.chatbot.dto.response.ChatbotRequest;
import com.Jejumate.Jejumate_BE.domain.chatbot.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping
    @Operation(summary = "챗봇과 대화하기", description = "사용자 메시지를 보내고, 챗봇의 응답을 받습니다.")
    public ResponseEntity<ChatbotResponse> chat(
            @RequestHeader("user-id") Long userId,
            @RequestBody ChatbotRequest request
    ) {
        ChatbotResponse response = chatbotService.sendJsonToChatbot(userId, request);

        return ResponseEntity.ok(response);
    }
}