package com.postgraduate.global.auth.login.exception;

import com.postgraduate.global.auth.login.presentation.constant.AuthResponseCode;
import com.postgraduate.global.auth.login.presentation.constant.AuthResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class OauthException extends ApplicationException {
    public OauthException() {
        super(AuthResponseMessage.NOT_FOUND_PROVIDER.getMessage(), AuthResponseCode.PROVIDER_NOT_FOUND.getCode());
    }
}
