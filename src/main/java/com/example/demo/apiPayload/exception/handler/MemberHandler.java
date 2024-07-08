package com.example.demo.apiPayload.exception.handler;


import com.example.demo.apiPayload.code.BaseErrorCode;
import com.example.demo.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);}

}
