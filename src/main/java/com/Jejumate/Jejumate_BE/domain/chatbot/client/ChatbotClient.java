package com.Jejumate.Jejumate_BE.domain.chatbot.client;

import com.Jejumate.Jejumate_BE.domain.chatbot.dto.request.ChatbotResponse;
import com.Jejumate.Jejumate_BE.domain.chatbot.dto.response.ChatbotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatbotClient {

    private final WebClient webClient;

    @Value("${ai.chatbot.url}")
    private String chatbotUrl;

    //챗봇에게 메시지 전송 및 응답 수신
    public ChatbotResponse sendChat(String userMessage) {

        ChatbotRequest request = ChatbotRequest.builder()
                .messages(List.of(ChatbotRequest.Message.builder()
                        .role("user")
                        .content(userMessage)
                        .build()))
                .build();

        log.info("챗봇 서버 요청: {}", userMessage);

        //FastAPI 호출
        ChatbotResponse response = webClient.post()
                .uri(chatbotUrl + "/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatbotResponse.class)
                .block();

        if (response == null) {
            log.error("챗봇 응답 null");
            throw new RuntimeException("챗봇 서버 통신 오류");
        }

        log.info("챗봇 응답 수신 - Action: {}", response.getAction());
        return response;
    }
}