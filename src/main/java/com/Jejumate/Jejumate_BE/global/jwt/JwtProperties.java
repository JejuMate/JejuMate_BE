package com.Jejumate.Jejumate_BE.global.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt") //application.yml 파일의 jwt으로 시작하는 모든 설정값을 필드에 주입
public class JwtProperties {
    private String secretKey; //jwt: secret-key의 값을 가져옴
    private long accessTokenValidity; //access-token-validity
    private long refreshTokenValidity; //refresh-token-validity
}
