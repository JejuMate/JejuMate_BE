package com.Jejumate.Jejumate_BE.domain.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false, unique = true, length = 500)
    private String refreshToken;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public RefreshToken(User user, String refreshToken, LocalDateTime expiredAt) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
        this.createdAt = LocalDateTime.now();
    }

    // ========== 비즈니스 로직 메서드 ==========
    public void updateToken(String newRefreshToken, LocalDateTime newExpiredAt) {
        this.refreshToken = newRefreshToken;
        this.expiredAt = newExpiredAt;
    }
}
