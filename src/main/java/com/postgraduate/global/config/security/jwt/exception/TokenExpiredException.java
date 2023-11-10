package com.postgraduate.global.config.security.jwt.exception;

import com.postgraduate.global.config.security.jwt.constant.JwtResponseCode;
import com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage;

public class TokenExpiredException extends JwtException {
    public TokenExpiredException() {
        super(JwtResponseMessage.EXPIRED_JWT.getMessage(), JwtResponseCode.EXPIRED_JWT.getCode());
    }
}
