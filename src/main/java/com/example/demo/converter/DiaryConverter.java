package com.example.demo.converter;

import com.example.demo.domain.AIComment;
import com.example.demo.domain.AIQuestion;
import com.example.demo.domain.Diary;
import com.example.demo.domain.Member;
import com.example.demo.web.dto.DiaryResponseDTO;
import com.example.demo.web.dto.MemberResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DiaryConverter {
    public static DiaryResponseDTO.AiCommentResultDTO aiCommentResultDTO(Diary diary){
        return DiaryResponseDTO.AiCommentResultDTO.builder()
                .moodId(diary.getMood().getId())
                .moodName(diary.getMood().getName())
                .aiCommentList(diary.getAICommentList().stream()
                        .map(AIComment::getContent)
                        .collect(Collectors.toList()))
                .title(diary.getTitle())
                .content(diary.getContent())
                .build();
    }

    public static DiaryResponseDTO.PlusDiaryResultDTO diaryListDTO(List<Diary> diaries) {
        List<DiaryResponseDTO.DiaryDTO> diaryDTOList = diaries.stream()
                .map(diary -> DiaryResponseDTO.DiaryDTO.builder()
                        .diaryId(diary.getId())
                        .title(diary.getTitle())
                        .content(diary.getContent())
                        .mood(diary.getMood())
                        .member(DiaryResponseDTO.MemberDTO.builder()
                                .memberId(diary.getMember().getId())
                                .username(diary.getMember().getUsername())
                                .ageGroup(diary.getMember().getAgeGroup())
                                .profileImage(diary.getMember().getProfileImage())
                                .keywordList(diary.getMember().getMemberKeywordList().stream()
                                        .map(memberKeyword->memberKeyword.getKeyword().getCategory())
                                        .collect(Collectors.toList()))
                                .build())
                        .build())
                .collect(Collectors.toList());

        return DiaryResponseDTO.PlusDiaryResultDTO.builder()
                .diaryList(diaryDTOList)
                .build();
    }

    public static DiaryResponseDTO.AIQuestionDTO aiQuestionDTO(AIQuestion aiQuestion){
        Long memberId = null;
        if (aiQuestion.getMemberQuestions() != null && !aiQuestion.getMemberQuestions().isEmpty()) {
            memberId = aiQuestion.getMemberQuestions().get(0).getMember().getId();
        }

        return DiaryResponseDTO.AIQuestionDTO.builder()
                .category(aiQuestion.getCategory())
                .content(aiQuestion.getContent())
                .memberId(memberId)
                .build();
    }
    }


