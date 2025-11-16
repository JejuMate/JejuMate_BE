package com.Jejumate.Jejumate_BE.domain.user.repository;

import com.Jejumate.Jejumate_BE.domain.user.domain.RefreshToken;
import com.Jejumate.Jejumate_BE.domain.user.domain.User;
import com.Jejumate.Jejumate_BE.domain.user.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //provider, providerId로 User 객체 조회
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

}
