package com.example.demo.Provider;

import com.example.demo.domain.Member;
import com.example.demo.service.SocialLoginService.CustomOAuth2User;
import com.example.demo.web.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Secure key generation
    private final long accessTokenExpirationTime = 24 * 60 * 60 * 1000; // 1 day
    private final long refreshTokenExpirationTime = 7 * 24 * 60 * 60 * 1000; // 7 days

    public JwtToken generateToken(CustomOAuth2User customOAuth2User) {
        Long memberId = customOAuth2User.getId();

        String accessToken = Jwts.builder()
                .setSubject(memberId.toString())
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(memberId.toString())
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(secretKey)
                .compact();

        return new JwtToken(accessToken, refreshToken);
    }

    public JwtToken generateToken(Member member) {
        Long memberId = member.getId();

        String accessToken = Jwts.builder()
                .setSubject(memberId.toString())
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(memberId.toString())
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(secretKey)
                .compact();

        return new JwtToken(accessToken, refreshToken);
    }

    public String validate(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt);
            return jwt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Key getSecretKey() {
        return secretKey;
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.get("memberId", Long.class);
    }
}
