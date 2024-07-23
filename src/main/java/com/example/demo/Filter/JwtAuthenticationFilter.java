package com.example.demo.Filter;

import com.example.demo.Provider.JwtProvider;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseToken(request);
            log.info("Parsed token: {}", token);
            if (token != null) {
                String email = jwtProvider.getEmailFromToken(token);
                log.info("Email from token: {}", email);

                if (email != null) {
                    Member member = memberRepository.findByEmail(email).orElse(null);

                    if (member != null) {
                        String role = member.getRole().getKey();
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(role));
                        log.info("사용자 권한 설정 - 이메일: {}, 권한: {}", email, role);
                        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        securityContext.setAuthentication(authenticationToken);
                        SecurityContextHolder.setContext(securityContext);
                    } else {
                        log.info("No member found for email: {}", email);
                    }
                } else {
                    log.info("Invalid token: email is null");
                }
            } else {
                log.info("No token found in request");
            }
        } catch (Exception e) {
            log.error("Error in JWT authentication filter", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}



