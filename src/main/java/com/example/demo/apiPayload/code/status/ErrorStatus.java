package com.example.demo.apiPayload.code.status;

import com.example.demo.apiPayload.code.BaseErrorCode;
import com.example.demo.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //토큰 관련 응답
    JWT_FORBIDDEN(HttpStatus.FORBIDDEN, "JWT400", "이미 로그아웃 된 토큰입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "JWT4001", "접근 권한이 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"JWT4002" ,"잘못된 요청 입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "JWT4003", "UnAuthorized"),
    JWT_BAD_REQUEST(HttpStatus.UNAUTHORIZED, "JWT4004","잘못된 JWT 서명입니다."),
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4005","액세스 토큰이 만료되었습니다."),
    JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4006","리프레시 토큰이 만료되었습니다. 다시 로그인하시기 바랍니다."),
    JWT_UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT4007","지원하지 않는 JWT 토큰입니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT4008","유효한 JWT 토큰이 없습니다."),

    //멤버 관련 응답
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND,"Key4004","키워드가 존재하지 않습니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"member4004","멤버가 존재하지 않습니다"),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "member4005", "멤버가 이미 존재합니다"),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "password4001", "비밀번호가 일치하지 않습니다"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "auth4001", "아이디 또는 비밀번호가 일치하지 않습니다"),
    INVALID_KEYWORD_IDS(HttpStatus.BAD_REQUEST, "keyword4006", "하나 이상의 키워드 ID가 유효하지 않습니다"),

    // AI 관련 응답
    AI_RESPONSE_NULL_OR_EMPTY(HttpStatus.BAD_REQUEST, "AI4007", "AI 응답이 null이거나 비어 있습니다");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    //message,code, isSuccess
    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }
    //message, code, isSuccess, httpStatus 반환
    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
