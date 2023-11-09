package com.postgraduate.global.jwt.exception;

import static com.postgraduate.global.jwt.constant.JwtResponseCode.NONE_REFRESH_TOKEN;
import static com.postgraduate.global.jwt.constant.JwtResponseMessage.NONE_REFRESH;

public class NoneRefreshTokenException extends JwtException {
    public NoneRefreshTokenException() {
        super(NONE_REFRESH.getMessage(), NONE_REFRESH_TOKEN.getCode());
    }
}
