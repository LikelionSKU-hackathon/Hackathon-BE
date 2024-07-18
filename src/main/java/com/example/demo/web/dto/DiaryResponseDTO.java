package com.example.demo.web.dto;

import com.example.demo.domain.AIComment;
import com.example.demo.domain.Diary;
import com.example.demo.domain.Mood;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class DiaryResponseDTO {
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
}
