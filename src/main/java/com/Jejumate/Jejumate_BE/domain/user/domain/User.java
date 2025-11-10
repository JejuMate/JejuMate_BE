package com.Jejumate.Jejumate_BE.domain.user.domain;

import com.Jejumate.Jejumate_BE.domain.user.enums.Provider;
import com.Jejumate.Jejumate_BE.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
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
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Builder
    public User(String email, Provider provider, String providerId) {
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }

    // ========== 비즈니스 로직 메서드 ==========
}
