package com.postgraduate.global.auth.login.exception;

import com.postgraduate.global.auth.login.presentation.constant.AuthResponseCode;
import com.postgraduate.global.auth.login.presentation.constant.AuthResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class KakaoCodeException extends ApplicationException {
    public KakaoCodeException() {
        super(AuthResponseMessage.KAKAO_CODE.getMessage(), AuthResponseCode.AUTH_KAKAO_CODE.getCode());
    }
}
