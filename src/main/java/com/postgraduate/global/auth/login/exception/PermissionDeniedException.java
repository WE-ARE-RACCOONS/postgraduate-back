package com.postgraduate.global.auth.login.exception;

import com.postgraduate.global.auth.login.presentation.constant.AuthResponseCode;
import com.postgraduate.global.auth.login.presentation.constant.AuthResponseMessage;
import com.postgraduate.global.exception.ApplicationException;


public class PermissionDeniedException extends ApplicationException {

    public PermissionDeniedException() {
        super(AuthResponseMessage.PERMISSION_DENIED.getMessage(), AuthResponseCode.AUTH_DENIED.getCode());
    }
}
