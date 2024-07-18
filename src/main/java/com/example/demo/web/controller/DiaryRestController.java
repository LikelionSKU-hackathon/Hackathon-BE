package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.DiaryConverter;
import com.example.demo.domain.Diary;
import com.example.demo.service.AIService.AICommentService;
import com.example.demo.web.dto.DiaryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryRestController {
    private final AICommentService aiCommentService;
    @GetMapping("/{diaryId}/ai")
    @Operation(summary="AI 댓글 조회 API", description="AI 댓글을 생성하고 조회하는 API")
    public ApiResponse<DiaryResponseDTO.AiCommentResultDTO> aicomment(@PathVariable(name="diaryId")Long diaryId){
        Diary diary = aiCommentService.generateAIComment(diaryId);
        return ApiResponse.onSuccess(DiaryConverter.aiCommentResultDTO(diary));
    }
}
