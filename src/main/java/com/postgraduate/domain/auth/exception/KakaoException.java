package com.postgraduate.domain.auth.exception;

import static com.postgraduate.domain.auth.presentation.contant.AuthResponseCode.AUTH_INVALID_KAKAO;
import static com.postgraduate.domain.auth.presentation.contant.AuthResponseMessage.KAKAO_INVALID_MESSAGE;

public class KakaoException extends AuthException{
    public KakaoException() {
        super(KAKAO_INVALID_MESSAGE.getMessage(), AUTH_INVALID_KAKAO.getCode());
    }
}