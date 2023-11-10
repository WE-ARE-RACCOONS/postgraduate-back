package com.postgraduate.global.jwt.exception;

import com.postgraduate.global.jwt.constant.JwtResponseCode;
import com.postgraduate.global.jwt.constant.JwtResponseMessage;

public class InvalidTokenException extends JwtException {
    public InvalidTokenException() {
        super(JwtResponseMessage.INVALID_JWT.getMessage(), JwtResponseCode.INVALID_JWT.getCode());
    }
}
