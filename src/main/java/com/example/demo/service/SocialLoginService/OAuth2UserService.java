package com.example.demo.service.SocialLoginService;

import com.example.demo.domain.Member;
import com.example.demo.domain.enums.Role;
import com.example.demo.domain.enums.SocialType;
import com.example.demo.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // 엑세스 토큰을 가져오는 부분
        String accessToken = oAuth2UserRequest.getAccessToken().getTokenValue(); // 엑세스 토큰 가져오기
        log.info("Access Token: {}", accessToken); // 엑세스 토큰을 로그로 출력
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String oauthClientName = oAuth2UserRequest.getClientRegistration().getClientName();
        final String[] userEmail = new String[1];
        final String[] userName = new String[1];

        try {
            log.info("OAuth2 User Attributes: {}", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            log.error("유저 정보 로딩 중 오류 발생", e);
        }

        if (oauthClientName.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            userEmail[0] = (String) response.get("email");
            userName[0] = (String) response.get("name");
        } else if (oauthClientName.equals("google")) {
            userEmail[0] = oAuth2User.getAttribute("email");
            userName[0] = oAuth2User.getAttribute("name");
        } else if (oauthClientName.equals("kakao")) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            userEmail[0] = (String) kakaoAccount.get("email");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            userName[0] = (String) profile.get("nickname");
        }

        // 기본값 설정
        if (userEmail[0] == null) {
            userEmail[0] = "default@example.com";
        }
        if (userName[0] == null) {
            userName[0] = "Unknown User";
        }

        Member member = memberRepository.findByEmail(userEmail[0]).orElseGet(() -> {
            Member newMember = Member.builder()
                    .username(userName[0])
                    .email(userEmail[0])
                    .role(Role.ROLE_USER)
                    .socialType(SocialType.valueOf(oauthClientName.toUpperCase()))
                    .profileImage("")
                    .ageGroup("")
                    .password("")
                    .build();
            return memberRepository.save(newMember);
        });

        Map<String, String> stringAttributes = new HashMap<>();
        stringAttributes.put("id", member.getId().toString());
        stringAttributes.put("name", userName[0]);
        stringAttributes.put("email", userEmail[0]);

        return new CustomOAuth2User(oAuth2User, stringAttributes);
    }
}


