package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.DiaryConverter;
import com.example.demo.domain.AIQuestion;
import com.example.demo.domain.Diary;
import com.example.demo.service.AIService.AICommentService;
import com.example.demo.service.DiaryService.DiaryQueryService;
import com.example.demo.web.dto.DiaryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryRestController {
    private final AICommentService aiCommentService;
    private final DiaryQueryService diaryQueryService;
    @GetMapping("/{diaryId}/aiComment")
    @Operation(summary="AI 댓글 조회 API", description="AI 댓글을 생성하고 조회하는 API")
    public ApiResponse<DiaryResponseDTO.AiCommentResultDTO> aicomment(@PathVariable(name="diaryId")Long diaryId){
        Diary diary = aiCommentService.generateAIComment(diaryId);
        return ApiResponse.onSuccess(DiaryConverter.aiCommentResultDTO(diary));
    }

    @GetMapping("/diaryList")
    @Operation(summary="더 많은 이야기 구경하기 API", description=" 다른 사용자들이 작성한 글을 조회하는 API")
    public ApiResponse<DiaryResponseDTO.PlusDiaryResultDTO> diaryList(){
        List<Diary> diaries = diaryQueryService.getDiaryList();
        return ApiResponse.onSuccess(DiaryConverter.diaryListDTO(diaries));
    }

    @GetMapping("/{memberId}/aiQuestion")
    @Operation(summary="AI 주제 생성 API", description="사용자 키워드 기반 AI 주제 생성 API")
    public ApiResponse<DiaryResponseDTO.AIQuestionDTO> aiQuestion(@PathVariable(name="memberId")Long memberId){
        AIQuestion aiQuestion = aiCommentService.generateAIQuestion(memberId);
        return ApiResponse.onSuccess(DiaryConverter.aiQuestionDTO(aiQuestion));
    }

    @GetMapping("/month/{year}/{month}/{memberId}")
    @Operation(summary="이번 달 나의 쓰임 API(이모지 조회)", description=" 이번 달 사용자 일기의 기분을 모아보는 API")
    public ApiResponse<DiaryResponseDTO.EmojiResultDTO> emoji(@PathVariable(name = "year") int year,
                                                              @PathVariable(name = "month") int month,
                                                              @PathVariable(name = "memberId") Long memberId) {

        DiaryResponseDTO.EmojiResultDTO result = diaryQueryService.getDiariesByMonth(year, month, memberId);
        return ApiResponse.onSuccess(result);
    }



}
