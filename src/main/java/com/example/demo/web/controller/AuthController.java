package com.example.demo.web.controller;

import com.example.demo.Provider.JwtProvider;
import com.example.demo.service.SocialLoginService.CustomOAuth2User;
import com.example.demo.web.dto.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final JwtProvider jwtProvider;

    @GetMapping("/login/oauth2/success")
    @ResponseBody
    public Map<String, String> loginSuccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("사용자가 인증되지 않았습니다.");
        }

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        JwtToken token = jwtProvider.generateToken(customOAuth2User);

        // Authorization 헤더 설정
        String authHeader = "Bearer " + token.getAccessToken();

        // 응답 본문 작성
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("username", customOAuth2User.getName());
        responseBody.put("accessToken", token.getAccessToken());
        responseBody.put("authorization", authHeader);

        // 로그 찍기
        log.info("OAuth2 로그인 성공 - 사용자: {}", customOAuth2User.getName());
        log.info("생성된 JWT 토큰: {}", token.getAccessToken());
        log.info("응답 헤더에 설정된 Authorization: {}", authHeader);

        return responseBody;
    }
}

