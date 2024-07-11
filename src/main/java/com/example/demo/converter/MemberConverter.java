package com.example.demo.converter;


import com.example.demo.domain.Keyword;
import com.example.demo.domain.Member;
import com.example.demo.domain.mapping.MemberKeyword;
import com.example.demo.web.dto.JwtToken;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemberConverter {
    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member){
        return MemberResponseDTO.JoinResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .username(member.getUsername())
                .build();
    }
    public static Member toMember(MemberRequestDTO.JoinDTO request, String encodedPassword){
        return Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .age(request.getAge())
                .password(encodedPassword)
                .confirmPassword(request.getConfirmPassword())
                .profileImage(request.getProfileImage())
                .memberKeywordList(new ArrayList<>())
                .build();
    }
    public static List<MemberKeyword> toMemberKeywordList(List<Keyword> keywordList){
        return keywordList.stream()
                .map(keyword ->
                        MemberKeyword.builder()
                                .keyword(keyword)
                                .build()
                ).collect(Collectors.toList());
    }
    public static MemberResponseDTO.MyPageDTO toMypageDTO(Member member) {
        return MemberResponseDTO.MyPageDTO.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .memberId(member.getId())
                .memberKeyword(member.getMemberKeywordList().stream()
                        .map(memberKeyword -> memberKeyword.getKeyword().getName())
                        .collect(Collectors.toList()))
                .profileImage(member.getProfileImage())
                .build();
    }
    public static Member toLoginMember(MemberRequestDTO.LoginDTO request){
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
    public static MemberResponseDTO.LoginResultDTO toLoginResultDTO(Member member , JwtToken jwtToken){
        return MemberResponseDTO.LoginResultDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
