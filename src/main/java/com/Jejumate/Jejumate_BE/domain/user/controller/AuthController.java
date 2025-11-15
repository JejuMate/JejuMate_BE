package com.Jejumate.Jejumate_BE.domain.user.controller;

import com.Jejumate.Jejumate_BE.domain.user.dto.AuthLoginResponse;
import com.Jejumate.Jejumate_BE.domain.user.dto.DevTokenRequest;
import com.Jejumate.Jejumate_BE.domain.user.dto.TokenRefreshRequest;
import com.Jejumate.Jejumate_BE.domain.user.dto.TokenRefreshResponse;
import com.Jejumate.Jejumate_BE.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    //카카오 로그인 콜백
    @GetMapping("/kakao/login")
    public ResponseEntity<AuthLoginResponse> kakaoLogin(
            @RequestParam("code") String authorizationCode
    ) {
        //AuthService에 인가 코드 전달하여 로그인/회원가입 로직 처리
        AuthLoginResponse response = authService.loginWithKakao(authorizationCode);
        return ResponseEntity.ok(response);
    }

    //Access Token 재발급
    //TODO: @CookieValue 사용하여 Refresh Token를 쿠키로 넣어주기 (웹 공격 방어 위해)
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> tokensRefresh(
            @RequestBody TokenRefreshRequest request
    ) {
        TokenRefreshResponse response = authService.tokensRefresh(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    //개발용 토큰 발급
    //TODO: @Profile("dev")
    @PostMapping("/dev-token")
    public ResponseEntity<AuthLoginResponse> createDevToken(
            @RequestBody DevTokenRequest request
    ) {
        AuthLoginResponse response = authService.createDevToken(request.userId());
        return ResponseEntity.ok(response);
    }
}