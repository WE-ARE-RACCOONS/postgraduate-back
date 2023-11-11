package com.postgraduate.domain.auth.exception;

import static com.postgraduate.domain.auth.presentation.contant.AuthResponseCode.AUTH_DENIED;
import static com.postgraduate.domain.auth.presentation.contant.AuthResponseMessage.PERMISSION_DENIED;


public class PermissionDeniedException extends AuthException {

    public PermissionDeniedException() {
        super(PERMISSION_DENIED.getMessage(), AUTH_DENIED.getCode());
    }
}
