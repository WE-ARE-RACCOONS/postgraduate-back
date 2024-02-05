package com.postgraduate.global.config.security.jwt.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.global.config.security.jwt.constant.JwtResponseCode.NOT_FOUND_REFRESH_TOKEN;
import static com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage.NOT_FOUND_REFRESH;

public class NoneRefreshTokenException extends ApplicationException {
    public NoneRefreshTokenException() {
        super(NOT_FOUND_REFRESH.getMessage(), NOT_FOUND_REFRESH_TOKEN.getCode());
    }
}