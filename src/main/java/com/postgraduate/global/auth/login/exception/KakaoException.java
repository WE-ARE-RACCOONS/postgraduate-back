package com.postgraduate.global.auth.login.exception;

import com.postgraduate.global.auth.login.presentation.constant.AuthResponseCode;
import com.postgraduate.global.auth.login.presentation.constant.AuthResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class KakaoException extends ApplicationException {
    public KakaoException() {
        super(AuthResponseMessage.KAKAO_INVALID.getMessage(), AuthResponseCode.AUTH_INVALID_KAKAO.getCode());
    }
}
