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
    // 회원가입 결과 값 반환 (memberId, 회원가입 날짜, 닉네임)
    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member){
        return MemberResponseDTO.JoinResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .username(member.getUsername())
                .build();
    }
    //회원가입한 멤버 생성(request로 받은 값을 member 객체로 생성)
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
    //social 회원가입을 위해 기존 회원가입 된 멤버의 정보 + 추가 정보를 받아 멤버 객체를 set
    public static void toSocialMember(MemberRequestDTO.SocialJoinDTO request, Member member, String profileImageUrl) {
        member.setUsername(request.getUsername());
        member.setAgeGroup(request.getAge_group());
        member.setProfileImage(profileImageUrl);

    }
    //Mypage controller를 위함
    public static MemberResponseDTO.MyPageDTO toMypageDTO(Member member) {
        return MemberResponseDTO.MyPageDTO.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .memberId(member.getId())
                .AgeGroup(member.getAgeGroup())
                .keywordList(member.getMemberKeywordList().stream()
                        .map(memberKeyword -> MemberResponseDTO.KeywordDTO.builder()
                                .emoji(memberKeyword.getKeyword().getEmoji())
                                .category(memberKeyword.getKeyword().getCategory())
                                .name(memberKeyword.getKeyword().getName())
                                .build())
                        .collect(Collectors.toList()))
                .profileImage(member.getProfileImage())
                .build();
    }
    // login을 위함
    public static Member toLoginMember(MemberRequestDTO.LoginDTO request){
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
    //login 결과값 반환
    public static MemberResponseDTO.LoginResultDTO toLoginResultDTO(Member member , JwtToken jwtToken){
        return MemberResponseDTO.LoginResultDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
    // keyword 조회 결과 값 반환
    public static MemberResponseDTO.KeywordResultDTO toKeywordResultDTO(Long memberId,List<Keyword> keywords){

        return MemberResponseDTO.KeywordResultDTO.builder()
                .memberId(memberId)
                .KeywordList(keywords)
                .build();
    }
    //keyword 선택 값 반환
    public static MemberResponseDTO.setKeywordResultDTO toSetKeywordResultDTO(Member member) {
        List<MemberResponseDTO.MemberKeywordDTO> keywordDTOList = member.getMemberKeywordList().stream()
                .map(memberKeyword -> MemberResponseDTO.MemberKeywordDTO.builder()
                        .keywordId(memberKeyword.getKeyword().getId())
                        .build())
                .collect(Collectors.toList());

        return MemberResponseDTO.setKeywordResultDTO.builder()
                .memberId(member.getId())
                .keywordList(keywordDTOList)
                .build();
    }



}
