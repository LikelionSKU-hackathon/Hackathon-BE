package com.example.demo.apiPayload.exception.handler;

import com.example.demo.apiPayload.code.BaseErrorCode;
import com.example.demo.apiPayload.exception.GeneralException;

public class KeywordHandler extends GeneralException {
    public KeywordHandler(BaseErrorCode errorCode) {
        super(errorCode);}
}
