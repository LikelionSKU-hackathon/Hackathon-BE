package com.example.demo.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@Builder
public class ReasonDTO {
    private HttpStatus httpStatus;

    private final String message;
    private final String code;
    private final boolean isSuccess;

    public boolean getIsSuccess(){return isSuccess;}
}