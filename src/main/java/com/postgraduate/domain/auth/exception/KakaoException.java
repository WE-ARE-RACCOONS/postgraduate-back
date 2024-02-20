package com.postgraduate.domain.auth.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.AUTH_INVALID_KAKAO;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.KAKAO_INVALID;

public class KakaoException extends ApplicationException {
    public KakaoException() {
        super(KAKAO_INVALID.getMessage(), AUTH_INVALID_KAKAO.getCode());
    }
}
