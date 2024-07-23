package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.MemberConverter;
import com.example.demo.domain.Keyword;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService.MemberCommandService;
import com.example.demo.service.MemberService.MemberQueryService;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRestController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    // 유효성 검사 적용 전 회원가입 API
    @PostMapping(value="/",consumes = "multipart/form-data")

    @Operation(summary="회원가입 API", description="회원가입하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join (@ModelAttribute @Valid MemberRequestDTO.JoinDTO request) {
        Member member = memberCommandService.joinMember(request);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    @PutMapping(value="/{memberId}",consumes = "multipart/form-data")

    @Operation(summary="소셜 회원가입 API", description="추가적인 소셜 회원가입하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join (@ModelAttribute @Valid MemberRequestDTO.SocialJoinDTO request, @PathVariable (name="memberId") Long memberId) {
        Member member = memberCommandService.SocialJoinMember(request, memberId);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    @GetMapping("/{memberId}")
    @Operation(summary="마이페이지 API", description="회원정보 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")

    })
    public ApiResponse<MemberResponseDTO.MyPageDTO> mypage (@PathVariable(name="memberId") Long memberId) {
        Member member = memberQueryService.getMypage(memberId);
        return ApiResponse.onSuccess(MemberConverter.toMypageDTO(member));
    }


    @PostMapping("/login")
    @Operation(summary="로그인 API", description="jwt를 이용한 API")
    public ApiResponse<MemberResponseDTO.LoginResultDTO> login (@RequestBody @Valid MemberRequestDTO.LoginDTO request) {
        MemberResponseDTO.LoginResultDTO loginResultDTO = memberCommandService.login(request);
        return ApiResponse.onSuccess(loginResultDTO);
    }


    @GetMapping("/keyword")
    @Operation(summary="키워드 목록 조회API", description="회원의 연령별 키워드 목록을 조회하는 API")
    public ApiResponse<MemberResponseDTO.KeywordResultDTO> keyword(@RequestParam(name="age_group") String age_group) {
        List<Keyword> keywords = memberQueryService.getKeyword(age_group);
        return ApiResponse.onSuccess(MemberConverter.toKeywordResultDTO(age_group, keywords));
    }


    @PostMapping("/{memberId}/keywords")
    @Operation(summary="키워드 선택 API", description="회원의 키워드 선택 API")
    public ApiResponse<MemberResponseDTO.setKeywordResultDTO> setKeyword(
            @RequestBody MemberRequestDTO.setKeywordDTO request,
            @PathVariable(name = "memberId") Long memberId) {

        Member member = memberCommandService.setKeyword(request, memberId);
        return ApiResponse.onSuccess(MemberConverter.toSetKeywordResultDTO(member));
    }

}
