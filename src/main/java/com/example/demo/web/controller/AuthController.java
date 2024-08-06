package com.example.demo.web.controller;

import com.example.demo.Provider.JwtProvider;
import com.example.demo.service.SocialLoginService.CustomOAuth2User;
import com.example.demo.web.dto.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/jwt")
    public ResponseEntity<Map<String, String>> getJwtToken(HttpServletRequest request) {
        JwtToken token = (JwtToken) request.getSession().getAttribute("jwtToken");
        String provider = (String) request.getSession().getAttribute("provider");
        if (token == null || provider == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", token.getAccessToken());
        response.put("authorization", "Bearer " + token.getAccessToken());
        response.put("provider", provider);
        return ResponseEntity.ok(response);
    }
}

