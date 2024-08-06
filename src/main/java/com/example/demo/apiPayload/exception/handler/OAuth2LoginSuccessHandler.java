package com.example.demo.apiPayload.exception.handler;

import com.example.demo.Provider.JwtProvider;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.SocialLoginService.CustomOAuth2User;
import com.example.demo.web.dto.JwtToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        JwtToken token = jwtProvider.generateToken(customOAuth2User);
        Member member = memberRepository.findById(customOAuth2User.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String redirectUrl;
        if (!member.isComplete()) {
            redirectUrl = "https://ssudamssudam.netlify.app/oauth2/redirect?token=" + token.getAccessToken();
        } else {
            redirectUrl = "https://ssudamssudam.netlify.app/oauth2/success?token=" + token.getAccessToken();
        }
        response.sendRedirect(redirectUrl);
    }
}
