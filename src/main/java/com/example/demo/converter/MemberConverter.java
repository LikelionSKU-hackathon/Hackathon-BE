package com.example.demo.converter;

import com.example.demo.domain.Member;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;

import java.time.LocalDateTime;

public class MemberConverter {
    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member){
        return MemberResponseDTO.JoinResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .name(member.getName())
                .build();
    }
    public static Member toMember(MemberRequestDTO.JoinDTO request){
        return Member.builder()
                .name(request.getName())
                .userId(request.getUserId())
                .email(request.getEmail())
                .age(request.getAge())
                .password(request.getPassword())
                .build();
    }
}
