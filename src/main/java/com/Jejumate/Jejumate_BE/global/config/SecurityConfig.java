package com.Jejumate.Jejumate_BE.global.config;

import com.Jejumate.Jejumate_BE.global.security.JwtAuthenticationFilter;
import com.Jejumate.Jejumate_BE.global.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity //Spring Security를 활성화합니다.
@EnableMethodSecurity //@PreAuthorize 어노테이션 기반 보안을 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    //헬퍼 클래스 Bean 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    //정적 리소스 (Swagger, H2, favicon 등)는 보안 필터 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/favicon.ico",
                "/error"
        );
    }

    //비밀번호 암호화 Bean 주입
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //Spring Security 메인 필터 체인
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //Stateless(JWT)를 위한 기본 설정
        http
                //CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                //폼 로그인, HTTP Basic 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                //세션 관리 정책을 STATELESS로 설정 (서버가 세션을 만들지 않음)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        //CORS, restAuthenticationEntryPoint(401 예외 핸들러) 설정
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(e -> e.authenticationEntryPoint(restAuthenticationEntryPoint));

        //API 경로별 접근 권한 설정
        http
                .authorizeHttpRequests(authz -> authz
                        //모든 요청 허용
                        .anyRequest().permitAll()
                );
        /*
        http
                .authorizeHttpRequests(authz -> authz
                        //로그인/회원가입 경로는 누구나 허용
                        .requestMatchers("/api/auth/**").permitAll()
                        //그 외 /api/** 경로는 인증된 사용자만 허용
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                );
        */

        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //CORS 설정 Bean (프론트엔드 연동)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        //프론트엔드 localhost:5173에서 오는 API 요청을 허용
        config.setAllowedOrigins(List.of("http://localhost:5173", "https://jejumate.com"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); //모든 API 경로에 이 CORS 설정을 적용
        return source;
    }
}