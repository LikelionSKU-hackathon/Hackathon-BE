package com.example.demo.apiPayload.exception.handler;

import com.example.demo.Provider.JwtProvider;
import com.example.demo.domain.Member;
import com.example.demo.service.SocialLoginService.CustomOAuth2User;
import com.example.demo.web.dto.JwtToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        JwtToken token = jwtProvider.generateToken(customOAuth2User);

        // Authorization 헤더 설정
        String authHeader = "Bearer " + token.getAccessToken();
        response.addHeader("Authorization", authHeader);




        // 로그 찍기
        log.info("OAuth2 로그인 성공 - 사용자: {}", customOAuth2User.getName());
        log.info("생성된 JWT 토큰: {}", token.getAccessToken());
        log.info("응답 헤더에 설정된 Authorization: {}", response.getHeader("Authorization"));

        // 리디렉션
        response.sendRedirect("http://localhost:8080/");
    }
}

