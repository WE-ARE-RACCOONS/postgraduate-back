package com.postgraduate.global.config.security.jwt.exception;

import static com.postgraduate.global.config.security.jwt.constant.JwtResponseCode.INVALID_REFRESH_TOKEN;
import static com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage.INVALID_REFRESH_JWT;

public class InvalidRefreshTokenException extends JwtException {
    public InvalidRefreshTokenException() {
        super(INVALID_REFRESH_JWT.getMessage(), INVALID_REFRESH_TOKEN.getCode());
    }
}
