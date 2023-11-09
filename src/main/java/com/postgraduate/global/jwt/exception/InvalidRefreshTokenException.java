package com.postgraduate.global.jwt.exception;

import static com.postgraduate.global.jwt.constant.JwtResponseCode.INVALID_REFRESH_TOKEN;
import static com.postgraduate.global.jwt.constant.JwtResponseMessage.INVALID_JWT;

public class InvalidRefreshTokenException extends JwtException {
    public InvalidRefreshTokenException() {
        super(INVALID_JWT.getMessage(), INVALID_REFRESH_TOKEN.getCode());
    }
}
