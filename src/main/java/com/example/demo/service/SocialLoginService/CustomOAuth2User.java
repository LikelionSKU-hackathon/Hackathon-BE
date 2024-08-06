package com.example.demo.service.SocialLoginService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomOAuth2User implements OAuth2User {
    private OAuth2User oauth2User;
    private Map<String, String> stringAttributes;
    private Long id; // 추가된 필드

    public CustomOAuth2User(OAuth2User oauth2User, Map<String, String> stringAttributes) {
        this.oauth2User = oauth2User;
        this.stringAttributes = stringAttributes;
        this.id = Long.valueOf(stringAttributes.get("id"));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User != null ? oauth2User.getAttributes() : Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User != null ? oauth2User.getAuthorities() : Collections.emptyList();
    }

    @Override
    public String getName() {
        return stringAttributes != null && stringAttributes.containsKey("name")
                ? stringAttributes.get("name")
                : oauth2User != null ? oauth2User.getName() : null;
    }

    public Long getId() {
        return id;
    }
}