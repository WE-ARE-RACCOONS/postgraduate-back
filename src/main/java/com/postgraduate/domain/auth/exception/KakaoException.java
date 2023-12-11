package com.postgraduate.domain.auth.exception;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.AUTH_INVALID_KAKAO;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.KAKAO_INVALID;

public class KakaoException extends AuthException{
    public KakaoException() {
        super(KAKAO_INVALID.getMessage(), AUTH_INVALID_KAKAO.getCode());
    }
}
