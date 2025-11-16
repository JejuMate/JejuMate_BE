package com.Jejumate.Jejumate_BE.global.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class KakaoClient {

    //application.yml에서 설정값 주입
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final WebClient webClient = WebClient.create();

    //인가 코드로 Access Token 요청하기
    public String getAccessToken(String code) {
        log.info("[KakaoClient] Access Token 요청: {}", code);

        //HTTP 요청 Body 생성
        BodyInserters.FormInserter<String> formData = BodyInserters
                .fromFormData("grant_type", "authorization_code")
                .with("client_id", clientId)
                .with("redirect_uri", redirectUri)
                .with("code", code)
                .with("client_secret", clientSecret);

        //WebClient로 POST 요청
        JsonNode response = webClient.post()
                .uri(tokenUri) //POST https://kauth.kakao.com/oauth/token
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(); //비동기 처리를 동기 방식으로 대기

        if (response == null) {
            log.error("[KakaoClient] Access Token 응답이 null입니다");
            //TODO: CustomException 던지기
            throw new RuntimeException("카카오 인증 실패: Access Token 응답 없음");
        }

        String accessToken = response.get("access_token").asText();
        log.info("[KakaoClient] Access Token 발급 성공");
        return accessToken;
    }

    //Access Token으로 사용자 정보 요청하기
    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        log.info("[KakaoClient] 사용자 정보 요청 시작");

        //WebClient로 GET 요청
        JsonNode response = webClient.get()
                .uri(userInfoUri) //GET https://kapi.kakao.com/v2/user/me
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) //헤더에 토큰 추가
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null) {
            log.error("[KakaoClient] 사용자 정보 응답이 null입니다");
            // TODO: CustomException 던지기
            throw new RuntimeException("카카오 인증 실패: 사용자 정보 응답 없음");
        }

        //JsonNode를 KakaoUserInfoResponse DTO로 변환
        KakaoUserInfoResponse userInfo = new KakaoUserInfoResponse(response);
        log.info("[KakaoClient] 사용자 정보 조회 성공 - Kakao ID: {}", userInfo.getId());
        return userInfo;
    }
}