package com.example.demo.web.controller;

import com.example.demo.apiPayload.ApiResponse;
import com.example.demo.converter.MemberConverter;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService.MemberCommandService;
import com.example.demo.service.MemberService.MemberQueryService;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRestController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    // 유효성 검사 적용 전 회원가입 API
    @PostMapping("/")
    @Operation(summary="회원가입 API", description="회원가입하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")

    })
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join (@RequestBody @Valid MemberRequestDTO.JoinDTO request) {
        Member member = memberCommandService.joinMember(request);
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
}
