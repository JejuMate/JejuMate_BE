package com.Jejumate.Jejumate_BE.domain.user.domain;

import com.Jejumate.Jejumate_BE.domain.user.enums.Provider;
import com.Jejumate.Jejumate_BE.domain.user.enums.UserStatus;
import com.Jejumate.Jejumate_BE.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Builder
    public User(String nickname, Provider provider, String providerId) {
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.status = UserStatus.ACTIVE;
    }

    // ========== 비즈니스 로직 메서드 ==========
    //활성 상태 확인
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    //활성으로 전환
    public void rejoin() {
        this.status = UserStatus.ACTIVE;
    }

    //비활성으로 전환
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }
}
