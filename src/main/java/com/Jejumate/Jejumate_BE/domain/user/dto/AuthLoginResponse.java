package com.Jejumate.Jejumate_BE.domain.user.dto;

//로그인 응답
public record AuthLoginResponse(
        String accessToken,
        String refreshToken,
        Long userId,
        String nickname,
        boolean isNewMember
) {
    public static AuthLoginResponse of(String accessToken, String refreshToken, Long userId, String nickname, boolean isNewMember) {
        return new AuthLoginResponse(accessToken, refreshToken, userId, nickname, isNewMember);
    }
}