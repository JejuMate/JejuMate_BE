package com.Jejumate.Jejumate_BE.domain.user.service;

import com.Jejumate.Jejumate_BE.domain.user.domain.RefreshToken;
import com.Jejumate.Jejumate_BE.domain.user.domain.User;
import com.Jejumate.Jejumate_BE.domain.user.dto.AuthLoginResponse;
import com.Jejumate.Jejumate_BE.domain.user.dto.TokenRefreshResponse;
import com.Jejumate.Jejumate_BE.domain.user.enums.Provider;
import com.Jejumate.Jejumate_BE.domain.user.exception.UserErrorCode;
import com.Jejumate.Jejumate_BE.domain.user.exception.UserException;
import com.Jejumate.Jejumate_BE.domain.user.repository.RefreshTokenRepository;
import com.Jejumate.Jejumate_BE.domain.user.repository.UserRepository;
import com.Jejumate.Jejumate_BE.global.jwt.JwtProperties;
import com.Jejumate.Jejumate_BE.global.jwt.JwtTokenProvider;
import com.Jejumate.Jejumate_BE.global.oauth2.KakaoClient;
import com.Jejumate.Jejumate_BE.global.oauth2.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    //슬라이딩 세션 만료 기준 (7일)
    private static final long REFRESH_TOKEN_EXPIRE_THRESHOLD = 7 * 24 * 60 * 60 * 1000L;

    //카카오 로그인 및 자동 회원가입
    @Transactional
    public AuthLoginResponse loginWithKakao(String authorizationCode) {
        log.info("[AuthService] 카카오 로그인 시작");

        //accessToken 발급
        String kakaoAccessToken = kakaoClient.getAccessToken(authorizationCode);
        //카카오에 사용자 정보 요청
        KakaoUserInfoResponse userInfo = kakaoClient.getUserInfo(kakaoAccessToken);

        String providerId = String.valueOf(userInfo.getId());
        boolean isNewMember = false;

        //사용자 조회
        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(Provider.KAKAO, providerId);
        User user;
        if (optionalUser.isEmpty()) {
            //회원이 아닌 경우 신규 가입
            log.info("[AuthService] 신규 사용자 등록 - Kakao ID: {}", providerId);
            isNewMember = true;
            user = userRepository.save(User.builder()
                    .email(userInfo.getEmail())
                    .provider(Provider.KAKAO)
                    .providerId(providerId)
                    .nickname(userInfo.getNickname())
                    .build());
        } else {
            //기존 회원
            user = optionalUser.get();
            log.info("[AuthService] 기존 사용자 로그인 - User ID: {}", user.getId());

            //비활성 회원인지 체크
            if (!user.isActive()) {
                log.info("[AuthService] 비활성 회원이 재가입 - User ID: {}", user.getId());
                user.rejoin(); //더티 체킹
            }
        }

        //jwt 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        //Refresh Token DB에 저장/갱신
        saveOrUpdateRefreshToken(user, refreshToken);

        //로그인 응답 정보 반환
        return AuthLoginResponse.of(accessToken, refreshToken, user.getId(), user.getNickname(), isNewMember);
    }

    //Access Token 재발급
    @Transactional
    public TokenRefreshResponse tokensRefresh(String providedRefreshToken) {
        //Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(providedRefreshToken)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_TOKEN)); //유효하지 않은 리프레시 토큰

        User user = refreshToken.getUser();
        if (!user.isActive()) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        //새 Access Token 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId());
        String newRefreshToken = providedRefreshToken;

        //슬라이딩 세션 (만료 7일 이내로 남았으면 Refresh Token도 갱신)
        if (jwtTokenProvider.isTokenExpiringSoon(providedRefreshToken, REFRESH_TOKEN_EXPIRE_THRESHOLD)) {
            log.info("[AuthService] Refresh Token 만료 임박하여 갱신");
            newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            //DB의 토큰 정보도 갱신 (더티 체킹)
            refreshToken.updateToken(newRefreshToken, calculateExpiryDate());
        }

        return TokenRefreshResponse.of(newAccessToken, newRefreshToken);
    }

    //개발용 토큰 발급
    //TODO: @Profile("dev") 등으로 개발 환경에서만 동작하도록 제한
    @Transactional
    public AuthLoginResponse createDevToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        saveOrUpdateRefreshToken(user, refreshToken);

        return AuthLoginResponse.of(accessToken, refreshToken, user.getId(), user.getNickname(),false);
    }

    // ========== 헬퍼 메서드 ==========
    //Refresh Token DB에 저장/갱신
    private void saveOrUpdateRefreshToken(User user, String refreshTokenString) {
        LocalDateTime expiryDate = calculateExpiryDate();

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        (token) -> token.updateToken(refreshTokenString, expiryDate),
                        () -> refreshTokenRepository.save(RefreshToken.builder()
                                .user(user)
                                .refreshToken(refreshTokenString)
                                .expiredAt(expiryDate)
                                .build())
                );
    }

    //Refresh Token 만료 시간 계산
    private LocalDateTime calculateExpiryDate() {
        long validityMs = jwtProperties.getRefreshTokenValidity();
        return LocalDateTime.now().plus(validityMs, ChronoUnit.MILLIS);
    }
}