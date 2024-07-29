package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.DiaryConverter;
import com.example.demo.domain.AIQuestion;
import com.example.demo.domain.Diary;
import com.example.demo.service.AIService.AICommentService;
import com.example.demo.service.DiaryCommandService.DiaryQueryService;
import com.example.demo.web.dto.DiaryResponseDTO;
import com.example.demo.service.CommentService;
import com.example.demo.service.DiaryService;
import com.example.demo.service.LikeService;
import com.example.demo.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryRestController {

    private final DiaryService diaryService;
    private final CommentService commentService;
    private final AICommentService aiCommentService;
    private final DiaryQueryService diaryQueryService;
    private final LikeService likeService;

    @PostMapping("/{memberId}/diaries")
    @Operation(summary = "일기 작성 API", description = "특정 회원의 새로운 일기 작성")
    public ResponseEntity<DiaryResponseDTO> createDiary(@PathVariable Long memberId, @RequestBody DiaryRequestDTO diaryRequestDTO) {
        diaryRequestDTO.setMemberId(memberId); // Set the memberId from the path variable
        DiaryResponseDTO diaryResponseDTO = diaryService.createDiary(diaryRequestDTO);
        return ResponseEntity.ok(diaryResponseDTO);
    }
    @GetMapping("/{diaryId}")
    @Operation(summary = "일기 조회 API", description = "특정 일기 ID를 통해 일기 항목 조회")
    public ResponseEntity<DiaryResponseDTO> getDiary(@PathVariable Long diaryId) {
        DiaryResponseDTO diaryResponseDTO = diaryService.getDiary(diaryId);
        return ResponseEntity.ok(diaryResponseDTO);
    }

    @GetMapping("/{memberId}/diaries")
    @Operation(summary = "회원 일기 조회 API", description = "특정 회원의 모든 일기 조회")
    public ResponseEntity<List<DiaryResponseDTO>> getDiariesByMember(@PathVariable Long memberId) {
        List<DiaryResponseDTO> diaryResponseDTOList = diaryService.getDiariesByMember(memberId);
        return ResponseEntity.ok(diaryResponseDTOList);
    }
    @GetMapping("/{diaryId}/ai")
    @Operation(summary = "AI 댓글 조회 API", description = "AI 댓글을 생성하고 조회하는 API")
    public ApiResponse<DiaryResponseDTO.AiCommentResultDTO> aicomment(@PathVariable(name = "diaryId") Long diaryId) {
        Diary diary = aiCommentService.generateAIComment(diaryId);
        return ApiResponse.onSuccess(DiaryConverter.aiCommentResultDTO(diary));
    }

    @GetMapping("/diaryList")
    @Operation(summary="더 많은 이야기 구경하기 API", description=" 다른 사용자들이 작성한 글을 조회하는 API")
    public ApiResponse<DiaryResponseDTO.PlusDiaryResultDTO> diaryList(){
        List<Diary> diaries = diaryQueryService.getDiaryList();
        return ApiResponse.onSuccess(DiaryConverter.diaryListDTO(diaries));
    }

    @GetMapping("/month/{year}/{month}")
    @Operation(summary="이번 달 나의 쓰임 API(이모지 조회)", description=" 이번 달 사용자 일기의 기분을 모아보는 API")
    public ApiResponse<DiaryResponseDTO.EmojiResultDTO> emoji(@PathVariable(name = "year") int year,
                                                              @PathVariable(name = "month") int month,
                                                              Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        DiaryResponseDTO.EmojiResultDTO result = diaryQueryService.getDiariesByMonth(year, month, memberId);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/{diaryId}/comments")
    @Operation(summary = "댓글 작성 API", description = "일기에 댓글을 작성")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long diaryId, @RequestBody CommentRequestDTO commentRequestDTO) {
        commentRequestDTO.setDiaryId(diaryId); // Set the diaryId from the path variable
        CommentResponseDTO commentResponseDTO = commentService.createComment(commentRequestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @GetMapping("/{diaryId}/comments")
    @Operation(summary = "댓글 조회 API", description = "일기의 모든 댓글을 조회")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByDiary(@PathVariable Long diaryId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByDiary(diaryId);
        return ResponseEntity.ok(comments);
    }
    @PostMapping("/{diaryId}/likes")
    @Operation(summary = "좋아요 추가 API", description = "특정 일기에 좋아요를 추가합니다.")
    public ResponseEntity<LikeResponseDTO> addLike(@PathVariable Long diaryId, @RequestBody LikeRequestDTO likeRequestDTO) {
        likeRequestDTO.setDiaryId(diaryId); // Set the diaryId from the path variable
        LikeResponseDTO likeResponseDTO = likeService.addLike(likeRequestDTO);
        return ResponseEntity.ok(likeResponseDTO);
    }

    @DeleteMapping("/{diaryId}/likes/{likeId}")
    @Operation(summary = "좋아요 삭제 API", description = "좋아요 삭제")
    public ResponseEntity<Void> removeLike(@PathVariable Long diaryId, @PathVariable Long likeId) {
        likeService.removeLike(likeId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/popular")
    @Operation(summary = "역대 좋아요 수가 가장 많은 일기 조회 API", description = "역대 좋아요 갯수가 가장 많은 일기 조회")
    public ResponseEntity<DiaryResponseDTO> getMostLikedDiary() {
        DiaryResponseDTO diaryResponseDTO = diaryService.getMostLikedDiary();
        return ResponseEntity.ok(diaryResponseDTO);
    }


}
