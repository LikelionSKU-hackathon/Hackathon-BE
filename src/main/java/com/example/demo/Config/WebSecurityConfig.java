package com.example.demo.Config;

import com.example.demo.Filter.JwtAuthenticationFilter;
import com.example.demo.apiPayload.exception.handler.OAuth2LoginSuccessHandler;
import com.example.demo.service.MemberService.MemberCommandService;
import com.example.demo.service.SocialLoginService.OAuth2UserService;
import com.example.demo.web.dto.MemberResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/api/v1/oauth2/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/members/**", "/health", "/diary/**","/keyword/**","/ai/**")
                        .permitAll()
                        .requestMatchers("/api/v1/user/secure-endpoint")
                        .hasRole("USER")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri("/api/v1/oauth2/authorization"))
                        .redirectionEndpoint(endpoint -> endpoint
                                .baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(oAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    //cors 설정 (모든 도메인 오픈)
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    // passwordEncoder 를 통해 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
