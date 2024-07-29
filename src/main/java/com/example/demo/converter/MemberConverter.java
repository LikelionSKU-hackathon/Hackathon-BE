package com.example.demo.converter;


import com.example.demo.domain.Keyword;
import com.example.demo.domain.Member;
import com.example.demo.domain.enums.Role;
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
    public static Member toMember(MemberRequestDTO.JoinDTO request, String encodedPassword, String profileImageUrl){
        return Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .ageGroup(request.getAge_group())
                .role(Role.valueOf(request.getRole()))
                .password(encodedPassword)

                .profileImage(profileImageUrl)
                .memberKeywordList(new ArrayList<>())
                .build();
    }
    public static void toSocialMember(MemberRequestDTO.SocialJoinDTO request, Member member, String profileImageUrl) {
        member.setUsername(request.getUsername());
        member.setAgeGroup(request.getAge_group());
        member.setProfileImage(profileImageUrl);


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

    public static MemberResponseDTO.KeywordResultDTO toKeywordResultDTO(Long memberId,List<Keyword> keywords){

        return MemberResponseDTO.KeywordResultDTO.builder()
                .memberId(memberId)
                .KeywordList(keywords)
                .build();
    }


    public static MemberResponseDTO.setKeywordResultDTO toSetKeywordResultDTO(Member member) {
        return MemberResponseDTO.setKeywordResultDTO.builder()
                .memberId(member.getId())
                .keywordList(member.getMemberKeywordList())
                .build();
    }



}
