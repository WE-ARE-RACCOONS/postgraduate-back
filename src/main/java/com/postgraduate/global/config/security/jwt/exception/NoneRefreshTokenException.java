package com.postgraduate.global.config.security.jwt.exception;

import com.postgraduate.global.config.security.jwt.constant.JwtResponseCode;
import com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage;

public class NoneRefreshTokenException extends JwtException {
    public NoneRefreshTokenException() {
        super(JwtResponseMessage.NONE_REFRESH.getMessage(), JwtResponseCode.NONE_REFRESH_TOKEN.getCode());
    }
}
