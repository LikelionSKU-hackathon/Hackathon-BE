package com.example.demo.web.dto;


import com.example.demo.domain.Keyword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.demo.domain.Keyword;
import com.example.demo.domain.mapping.MemberKeyword;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDTO{
        Long memberId;
        String username;
        LocalDateTime createdAt;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDTO{
        Long memberId;
        String username;
        String email;
        String AgeGroup;
        List<KeywordDTO> keywordList;
        String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordDTO{
        String emoji;
        String category;
        String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResultDTO{
        Long memberId;
        String email;
        String accessToken;
        String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordResultDTO{
        Long memberId;
        List<Keyword> KeywordList;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class setKeywordResultDTO {
        private Long memberId;
        private List<MemberKeywordDTO> keywordList;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberKeywordDTO {
        private Long keywordId;

    }




}
