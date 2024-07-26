package com.example.demo.web.dto;


import com.example.demo.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.demo.domain.AIComment;
import com.example.demo.domain.Diary;
import com.example.demo.domain.Mood;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryResponseDTO {
    private Long id;
    private String title;
    private String content;
    private boolean isPublic;
    private String memberUsername;
    private int likeCount;
    private String moodName;
    private String moodImage;
    LocalDateTime createdAt;
    private List<String> aiComments;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AiCommentResultDTO{
        Long moodId;
        String moodName;
        String title;
        String content;
        List<String> aiCommentList;


    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlusDiaryResultDTO {
        List<DiaryDTO> diaryList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryDTO {
        Long diaryId;
        String title;
        String content;
        Mood mood;
        MemberDTO member;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDTO {
        Long memberId;
        String username;
        String ageGroup;
        String profileImage;
        List<String> keywordList;
    }



}
