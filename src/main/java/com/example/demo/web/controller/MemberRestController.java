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
import org.springframework.security.core.Authentication;
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request, 요청 파라미터 오류"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflict, 멤버가 이미 존재함"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized, 비밀번호 불일치")
    })
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join (@ModelAttribute @Valid MemberRequestDTO.JoinDTO request) {
        Member member = memberCommandService.joinMember(request);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    @PutMapping(value="/{memberId}",consumes = "multipart/form-data")

    @Operation(summary="소셜 회원가입 API", description="추가적인 소셜 회원가입하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",description = "NotFound, 찾을 수 없음")
    })
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join (@ModelAttribute @Valid MemberRequestDTO.SocialJoinDTO request, @PathVariable (name="memberId") Long memberId) {
        Member member = memberCommandService.SocialJoinMember(request, memberId);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    @GetMapping("/")
    @Operation(summary="마이페이지 API", description="회원정보 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    public ApiResponse<MemberResponseDTO.MyPageDTO> mypage(Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        Member member = memberQueryService.getMypage(memberId);
        return ApiResponse.onSuccess(MemberConverter.toMypageDTO(member));
    }


    @PostMapping("/login")
    @Operation(summary="로그인 API", description="jwt를 이용한 API")
    public ApiResponse<MemberResponseDTO.LoginResultDTO> login (@RequestBody @Valid MemberRequestDTO.LoginDTO request) {
        MemberResponseDTO.LoginResultDTO loginResultDTO = memberCommandService.login(request);
        return ApiResponse.onSuccess(loginResultDTO);
    }


    @GetMapping("/checkEmail/{email}")
    @Operation(summary = "이메일 중복 조회 API", description = "중복된 이메일이 있는지 조회하는 API")
    public ApiResponse<Boolean> checkEmail(@PathVariable(name="email") String email) {
        Boolean result = memberQueryService.checkEmail(email);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/checkUsername/{username}")
    @Operation(summary="닉네임 중복 조회 API", description="중복된 닉네임이 있는지 조회하는 API")
    public ApiResponse<Boolean> checkUsername(@PathVariable(name="username") String username) {
        Boolean result = memberQueryService.checkUsername(username);
        return ApiResponse.onSuccess(result);
    }

}
