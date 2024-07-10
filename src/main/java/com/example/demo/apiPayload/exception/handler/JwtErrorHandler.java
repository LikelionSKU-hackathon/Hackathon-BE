package com.example.demo.apiPayload.exception.handler;

import com.example.demo.apiPayload.code.BaseErrorCode;
import com.example.demo.apiPayload.exception.GeneralException;

public class JwtErrorHandler extends GeneralException {
    public JwtErrorHandler(BaseErrorCode errorCode) {
        super(errorCode);}
}
