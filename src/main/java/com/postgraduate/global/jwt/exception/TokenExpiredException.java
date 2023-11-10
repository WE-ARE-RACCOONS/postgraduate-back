package com.postgraduate.global.jwt.exception;

import com.postgraduate.global.jwt.constant.JwtResponseCode;
import com.postgraduate.global.jwt.constant.JwtResponseMessage;

public class TokenExpiredException extends JwtException {
    public TokenExpiredException() {
        super(JwtResponseMessage.EXPIRED_JWT.getMessage(), JwtResponseCode.EXPIRED_JWT.getCode());
    }
}
