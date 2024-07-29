package com.example.demo.converter;

import com.example.demo.domain.AIComment;
import com.example.demo.domain.AIQuestion;
import com.example.demo.domain.Diary;
import com.example.demo.domain.Member;
import com.example.demo.web.dto.DiaryResponseDTO;
import com.example.demo.web.dto.MemberResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class DiaryConverter {
    //  AIComment 결과값 반환(moodId, moodName, aiCommentList(ai comment 1개만 받도록), diaryTitle, diaryContent)
    public static DiaryResponseDTO.AiCommentResultDTO aiCommentResultDTO(Diary diary){
        return DiaryResponseDTO.AiCommentResultDTO.builder()
                .moodId(diary.getMood().getId())
                .moodName(diary.getMood().getName())
                .aiCommentList(diary.getAiComment() != null ? List.of(diary.getAiComment().getContent()) : null) // 단일 AIComment 처리
                .title(diary.getTitle())
                .content(diary.getContent())
                .build();
    }
    // 더 많은 이야기 구경하기( diaryList에  diaryId, title, content, mood, member(id,name,profile, keywordList) 등 일부 내용만 받도록 설정)
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
    //aiQuestion 결과값 반환(aiQuestion의 카테고리와, 질문 내용, 해당 멤버의 값 반환)
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
    //이모지 조회(diaryId( 다이어리 조회를 위함), moodImage(이미지 조회), day(날짜를 기준으로))
    public DiaryResponseDTO.EmojiDTO convertToEmojiDto(Diary diary) {
        return DiaryResponseDTO.EmojiDTO.builder()
                .DiaryId(diary.getId())
                .MoodImage(diary.getMood().getMoodImage())
                .day(diary.getCreatedAt())
                .build();
    }


}


