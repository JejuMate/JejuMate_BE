package com.Jejumate.Jejumate_BE.global.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

    private Long id; //카카오가 발급한 고유 ID
    private String nickname; //카카오 닉네임

    //JSON 응답을 파싱하기 위한 생성자
    public KakaoUserInfoResponse(JsonNode node) {
        this.id = node.get("id").asLong();

        //properties.nickname 경로에서 값을 추출
        JsonNode properties = node.path("properties");

        //닉네임 선택 동의
        this.nickname = properties.path("nickname").asText(null);
    }
}