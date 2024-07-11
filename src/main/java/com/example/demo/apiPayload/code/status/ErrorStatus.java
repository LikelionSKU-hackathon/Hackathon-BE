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


    JWT_FORBIDDEN(HttpStatus.FORBIDDEN, "JWT400", "이미 로그아웃 된 토큰입니다."),
    //FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "JWT4001", "접근 권한이 없습니다."),
    //BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"JWT4002" ,"잘못된 요청 입니다."),
    //UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "JWT4003", "UnAuthorized"),
    //UNAUTHORIZED
    JWT_BAD_REQUEST(HttpStatus.UNAUTHORIZED, "JWT4004","잘못된 JWT 서명입니다."),
    //UNAUTHORIZED
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4005","액세스 토큰이 만료되었습니다."),
    //UNAUTHORIZED
    JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4006","리프레시 토큰이 만료되었습니다. 다시 로그인하시기 바랍니다."),
    //UNAUTHORIZED
    JWT_UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT4007","지원하지 않는 JWT 토큰입니다."),
    //UNAUTHORIZED
    JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT4008","유효한 JWT 토큰이 없습니다."),
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND,"Key4004","키워드가 존재하지 않습니다"),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"member4004","멤버가 존재하지 않습니다");

    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

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
