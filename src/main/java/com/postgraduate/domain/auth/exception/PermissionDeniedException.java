package com.postgraduate.domain.auth.exception;

import org.springframework.http.HttpStatus;

import static com.postgraduate.domain.auth.presentation.contant.AuthResponseCode.AUTH_NONE;
import static com.postgraduate.domain.auth.presentation.contant.AuthResponseMessage.PERMISSION_DENIED_MESSAGE;


public class PermissionDeniedException extends AuthException {

    public PermissionDeniedException() {
        super(PERMISSION_DENIED_MESSAGE.getMessage(), AUTH_NONE.getCode(), HttpStatus.FORBIDDEN);
    }
}
