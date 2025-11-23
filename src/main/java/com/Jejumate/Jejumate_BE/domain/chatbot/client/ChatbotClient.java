package com.Jejumate.Jejumate_BE.domain.chatbot.client;

import com.Jejumate.Jejumate_BE.domain.chatbot.dto.request.ChatbotResponse;
import com.Jejumate.Jejumate_BE.domain.chatbot.dto.response.AiChatbotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatbotClient {

    private final WebClient webClient;

    @Value("${ai.chatbot.url}")
    private String chatbotUrl;

    //챗봇에게 메시지 전송 및 응답 수신
    public ChatbotResponse sendChat(Object requestBody) {
        ChatbotResponse response;

        try {
                response = webClient.post()
                .uri(chatbotUrl + "/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ChatbotResponse.class)
                .block();
        } catch (WebClientResponseException e) {
            //서버 에러 이유 로그로 찍기
            log.error("[ChatbotClient] 챗봇 서버 에러 응답: {}", e.getResponseBodyAsString());
            throw e;
        }

        if (response == null) {
            log.error("[ChatbotClient] 챗봇 응답이 null입니다.");
            throw new RuntimeException("챗봇 서버 통신 오류");
        }

        log.info("[ChatbotClient] 챗봇 응답 수신. Action: {}", response.getAction());
        return response;
    }
}