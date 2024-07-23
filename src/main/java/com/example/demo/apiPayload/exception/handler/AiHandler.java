package com.example.demo.apiPayload.exception.handler;

import com.example.demo.apiPayload.code.BaseErrorCode;
import com.example.demo.apiPayload.exception.GeneralException;

public class AiHandler extends GeneralException {
    public AiHandler(BaseErrorCode errorCode) {
        super(errorCode);}
}
