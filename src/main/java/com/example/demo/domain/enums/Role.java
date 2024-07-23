package com.example.demo.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER", "User"),
    ROLE_GUEST("ROLE_GUEST", "Guest");

    private final String key;
    private final String title;
    @Override
    public String toString() {
        return key;
    }

}
