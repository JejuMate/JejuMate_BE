package com.Jejumate.Jejumate_BE.domain.user.dto;

//토큰 재발급 응답 DTO (슬라이딩 세션)
public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {
    public static TokenRefreshResponse of(String accessToken, String refreshToken) {
        return new TokenRefreshResponse(accessToken, refreshToken);
    }
}
