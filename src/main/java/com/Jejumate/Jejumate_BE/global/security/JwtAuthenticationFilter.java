package com.Jejumate.Jejumate_BE.global.security;

import com.Jejumate.Jejumate_BE.domain.user.exception.UserException;
import com.Jejumate.Jejumate_BE.global.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    //필터 로직
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        //Request Header에서 토큰 추출
        String jwt = resolveToken(request);

        //토큰 검증
        if (StringUtils.hasText(jwt)) { //토큰이 존재하는지 확인
            try {
                //유효성 검사
                jwtTokenProvider.validateToken(jwt);

                //토큰이 유효하면 Authentication 객체(인증객체)를 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

                //SecurityContext에 인증 객체를 저장
                //Controller에서 @AuthenticationPrincipal로 인증정보 꺼내쓸 수 있게됨
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), request.getRequestURI());

            } catch (UserException e) {
                //예외 발생 시 SecurityContext 비우기 (선택사항)
                SecurityContextHolder.clearContext();
                log.warn("JWT 인증 실패: {}, uri: {}", e.getMessage(), request.getRequestURI());
            }
        } else {
            //토큰이 없는 경우
        }

        //다음 필터로 요청/응답을 넘기기
        filterChain.doFilter(request, response);
    }

    //Request Header에서 Bearer 접두사 제거하고 토큰 값만 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}