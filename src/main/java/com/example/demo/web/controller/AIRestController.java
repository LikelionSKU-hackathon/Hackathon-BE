package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.DiaryConverter;
import com.example.demo.domain.AIQuestion;
import com.example.demo.domain.Diary;
import com.example.demo.service.AIService.AICommentService;
import com.example.demo.web.dto.DiaryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIRestController {
    private final AICommentService aiCommentService;
    @GetMapping("/question")

    @Operation(summary="AI 주제 생성 API", description="사용자 키워드 기반 AI 주제 생성 API")
    public ApiResponse<DiaryResponseDTO.AIQuestionDTO> aiQuestion(Authentication authentication){
        Long memberId = (Long) authentication.getPrincipal();
        AIQuestion aiQuestion = aiCommentService.generateAIQuestion(memberId);
        return ApiResponse.onSuccess(DiaryConverter.aiQuestionDTO(aiQuestion,memberId));
    }
    @GetMapping("comment/{diaryId}")
    @Operation(summary="AI 댓글 생성", description="AI 댓글을 생성하는 API")
    public ApiResponse<DiaryResponseDTO.AiCommentResultDTO> aicomment(@PathVariable(name="diaryId")Long diaryId){
        Diary diary = aiCommentService.generateAIComment(diaryId);
        return ApiResponse.onSuccess(DiaryConverter.aiCommentResultDTO(diary));
    }
    @GetMapping("spicyComment/{diaryId}")
    @Operation(summary="Spicy AI 댓글 생성", description="Spicy AI 댓글을 생성하는 API")
    public ApiResponse<DiaryResponseDTO.SpicyAiCommentResultDTO> spicyAiComment(@PathVariable(name="diaryId") Long diaryId){
        Diary diary = aiCommentService.generateSpicyAIComment(diaryId);
        return ApiResponse.onSuccess(DiaryConverter.spicyAiCommentResultDTO(diary));
    }

    @GetMapping("spicyComment/view/{diaryId}")
    @Operation(summary="Spicy AI 댓글 조회", description="특정 일기의 Spicy AI 댓글을 조회하는 API")
    public ApiResponse<DiaryResponseDTO.SpicyAiCommentResultDTO> viewSpicyAiComment(@PathVariable(name="diaryId") Long diaryId) {
        Diary diary = aiCommentService.getDiaryWithSpicyComment(diaryId);
        return ApiResponse.onSuccess(DiaryConverter.spicyAiCommentResultDTO(diary));
    }
}
