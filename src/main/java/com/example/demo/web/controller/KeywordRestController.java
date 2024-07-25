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
    public ApiResponse<MemberResponseDTO.KeywordResultDTO> keyword(@RequestParam(name="age_group") String age_group) {
        List<Keyword> keywords = memberQueryService.getKeyword(age_group);
        return ApiResponse.onSuccess(MemberConverter.toKeywordResultDTO(age_group, keywords));
    }
    @PostMapping("/{memberId}")
    @Operation(summary="키워드 선택 API", description="회원의 키워드 선택 API")
    public ApiResponse<MemberResponseDTO.setKeywordResultDTO> setKeyword(
            @RequestBody MemberRequestDTO.setKeywordDTO request,
            @PathVariable(name = "memberId") Long memberId) {

        Member member = memberCommandService.setKeyword(request, memberId);
        return ApiResponse.onSuccess(MemberConverter.toSetKeywordResultDTO(member));
    }
}
