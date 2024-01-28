package com.postgraduate.domain.auth.exception;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.PROVIDER_NOT_FOUND;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.NOT_FOUND_PROVIDER;

public class OauthException extends AuthException {
    public OauthException() {
        super(NOT_FOUND_PROVIDER.getMessage(), PROVIDER_NOT_FOUND.getCode());
    }
}
