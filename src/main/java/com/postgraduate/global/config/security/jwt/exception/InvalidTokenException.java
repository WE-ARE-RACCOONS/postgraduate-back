package com.postgraduate.global.config.security.jwt.exception;

import com.postgraduate.global.config.security.jwt.constant.JwtResponseCode;
import com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class InvalidTokenException extends ApplicationException {
    public InvalidTokenException() {
        super(JwtResponseMessage.INVALID_JWT.getMessage(), JwtResponseCode.INVALID_JWT.getCode());
    }
}
