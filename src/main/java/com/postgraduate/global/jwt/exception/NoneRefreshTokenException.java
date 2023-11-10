package com.postgraduate.global.jwt.exception;

import com.postgraduate.global.config.security.jwt.exception.JwtException;

import static com.postgraduate.global.config.security.jwt.constant.JwtResponseCode.NOT_FOUND_REFRESH_TOKEN;
import static com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage.NOT_FOUND_REFRESH;

public class NoneRefreshTokenException extends JwtException {
    public NoneRefreshTokenException() {
        super(NOT_FOUND_REFRESH.getMessage(), NOT_FOUND_REFRESH_TOKEN.getCode());
    }
}
