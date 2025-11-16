package com.Jejumate.Jejumate_BE.global.jwt;

import com.Jejumate.Jejumate_BE.domain.user.domain.User;
import com.Jejumate.Jejumate_BE.domain.user.exception.UserErrorCode;
import com.Jejumate.Jejumate_BE.domain.user.exception.UserException;
import com.Jejumate.Jejumate_BE.domain.user.repository.UserRepository;
import com.Jejumate.Jejumate_BE.global.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private SecretKey key; //암호화된 비밀키
    private final JwtProperties jwtProperties; //yml의 JWT 설정값을 주입받는 객체
    private final UserRepository userRepository; //DB 조회를 위한 Repository

    //비밀키 초기화 메서드
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //AccessToken 생성
    public String createAccessToken(Long userId) {
        return createToken(userId, jwtProperties.getAccessTokenValidity());
    }

    //RefreshToken 생성
    public String createRefreshToken(Long userId) {
        return createToken(userId, jwtProperties.getRefreshTokenValidity());
    }

    //JWT 생성
    private String createToken(Long userId, long validity) {
        //Claims
        Claims claims = Jwts.claims().subject(String.valueOf(userId)).build();

        //발행시간, 만료시간 설정
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity);

        //토큰 생성 및 서명
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key) //HMAC-SHA 알고리즘 및 비밀키로 서명
                .compact();
    }

    //JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        //null 또는 공백 처리
        if (token == null || token.isBlank()) {
            log.warn("JWT validateToken: token is null or blank");
            throw new UserException(UserErrorCode.JWT_IS_NULL);
        }

        //Bearer 접두어 처리
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
            if (token.isEmpty()) {
                log.warn("JWT validateToken: 'Bearer ' prefix present but no token");
                throw new UserException(UserErrorCode.JWT_IS_NULL);
            }
        }

        //토큰 파싱 및 검증
        try {
            Jwts.parser()
                    .verifyWith(key) //비밀키로 서명 검증
                    .build()
                    .parseSignedClaims(token);
            return true; //검증 성공
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (io.jsonwebtoken.security.SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }
        return false; //검증 실패
    }

    //토큰에서 userId 추출
    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }

    //ACCESS Token으로 Spring Security 인증 객체 생성
    public Authentication getAuthentication(String accessToken) {
        //토큰에서 userId 추출
        Long userId = this.getUserId(accessToken);

        //DB에서 userId로 User 객체를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        //인증된 사용자의 정보를 담을 UserDetails 객체 생성
        UserDetails userDetails = new CustomUserDetails(user.getId());

        //UserDetails 객체로 Authentication(인증 객체) 생성
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //refreshToken 토큰 만료 임박 확인 (슬라이딩 세션을 위해 추가)
    public boolean isTokenExpiringSoon(String refreshToken, long thresholdMillis) {
        try {
            if (refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7).trim();
            }

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            Date expiration = claims.getExpiration();
            long now = System.currentTimeMillis();

            //(만료 시간 - 현재 시간) < 기준 시간(7일)
            return expiration.getTime() - now < thresholdMillis;

        } catch (JwtException e) {
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        }
    }
}