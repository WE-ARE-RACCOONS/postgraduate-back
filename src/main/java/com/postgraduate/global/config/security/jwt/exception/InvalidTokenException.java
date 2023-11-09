package com.postgraduate.global.config.security.jwt.exception;

import com.postgraduate.global.config.security.jwt.constant.JwtResponseCode;
import com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage;

public class InvalidTokenException extends JwtException {
    public InvalidTokenException() {
        super(JwtResponseMessage.INVALID_JWT.getMessage(), JwtResponseCode.INVALID_JWT.getCode());
    }
}
