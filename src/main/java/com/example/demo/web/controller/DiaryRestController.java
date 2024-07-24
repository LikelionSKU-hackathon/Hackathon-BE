package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.DiaryConverter;
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
    @GetMapping("/{diaryId}/ai")
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


}
