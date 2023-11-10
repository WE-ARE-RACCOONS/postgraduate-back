package com.postgraduate.domain.auth.exception;

import static com.postgraduate.domain.auth.presentation.contant.AuthResponseCode.AUTH_KAKAO_CODE;
import static com.postgraduate.domain.auth.presentation.contant.AuthResponseMessage.KAKAO_CODE;

public class KakaoCodeException extends AuthException{
    public KakaoCodeException() {
        super(KAKAO_CODE.getMessage(), AUTH_KAKAO_CODE.getCode());
    }
}
