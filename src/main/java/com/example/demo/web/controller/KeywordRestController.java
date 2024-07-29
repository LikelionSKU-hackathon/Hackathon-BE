package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.MemberConverter;
import com.example.demo.domain.Keyword;
import com.example.demo.domain.Member;
import com.example.demo.service.MemberService.MemberCommandService;
import com.example.demo.service.MemberService.MemberQueryService;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/keyword")
public class KeywordRestController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    @GetMapping("/")
    @Operation(summary="키워드 목록 조회API", description="회원의 연령별 키워드 목록을 조회하는 API")
    public ApiResponse<MemberResponseDTO.KeywordResultDTO> keyword(Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        List<Keyword> keywords = memberQueryService.getKeyword(memberId);
        return ApiResponse.onSuccess(MemberConverter.toKeywordResultDTO(memberId, keywords));
    }
    @PostMapping("/")
    @Operation(summary="키워드 선택 API", description="회원의 키워드 선택 API")
    public ApiResponse<MemberResponseDTO.setKeywordResultDTO> setKeyword(
            @RequestBody MemberRequestDTO.setKeywordDTO request,
            Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        Member member = memberCommandService.setKeyword(request, memberId);
        return ApiResponse.onSuccess(MemberConverter.toSetKeywordResultDTO(member));
    }
}
