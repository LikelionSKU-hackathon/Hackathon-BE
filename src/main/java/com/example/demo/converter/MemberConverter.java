package com.example.demo.converter;

import com.example.demo.auth.JwtToken;
import com.example.demo.domain.Member;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;

import java.time.LocalDateTime;

public class MemberConverter {
    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member){
        return MemberResponseDTO.JoinResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .username(member.getUsername())
                .build();
    }
    public static Member toMember(MemberRequestDTO.JoinDTO request){
        return Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .age(request.getAge())
                .password(request.getPassword())
                .build();
    }
    public static Member toLoginMember(MemberRequestDTO.LoginDTO request){
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
    public static MemberResponseDTO.LoginResultDTO toLoginResultDTO(Member member ,JwtToken jwtToken){
        return MemberResponseDTO.LoginResultDTO.builder()
                .memberId(member.getId())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
