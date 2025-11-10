package com.Jejumate.Jejumate_BE.domain.user.repository;

import com.Jejumate.Jejumate_BE.domain.user.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    //RefreshToken 문자열로 RefreshToken 객체 조회
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    //userId로 RefreshToken 객체 조회 (로그아웃 시 사용)
    Optional<RefreshToken> findByUserId(Long userId);
}
