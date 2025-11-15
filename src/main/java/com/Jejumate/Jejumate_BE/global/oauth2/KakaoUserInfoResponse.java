package com.Jejumate.Jejumate_BE.global.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

    private Long id; //카카오가 발급한 고유 ID
    private String nickname; // 카오 닉네임
    private String email; //카카오 계정 이메일

    //JSON 응답을 파싱하기 위한 생성자
    public KakaoUserInfoResponse(JsonNode node) {
        this.id = node.get("id").asLong();

        //properties.nickname, kakao_account.email 경로에서 값을 추출
        JsonNode properties = node.path("properties");
        JsonNode kakaoAccount = node.path("kakao_account");

        //닉네임, 이메일 선택 동의
        this.nickname = properties.path("nickname").asText(null);
        this.email = kakaoAccount.path("email").asText(null);
    }
}