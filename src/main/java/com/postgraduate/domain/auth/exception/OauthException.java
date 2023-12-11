package com.postgraduate.domain.auth.exception;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.NONE_PROVIDER;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.PROVIDER_NONE;

public class OauthException extends AuthException{
    public OauthException() {
        super(PROVIDER_NONE.getMessage(), NONE_PROVIDER.getCode());
    }
}
