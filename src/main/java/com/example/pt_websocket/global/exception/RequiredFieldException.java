package com.example.pt_websocket.global.exception;


import com.example.pt_websocket.global.enums.ErrorCode;

public class RequiredFieldException extends GlobalException {
    public RequiredFieldException(ErrorCode errorCode) {
        super(errorCode);
    }
}
