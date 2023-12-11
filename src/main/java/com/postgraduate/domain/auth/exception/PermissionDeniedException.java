package com.postgraduate.domain.auth.exception;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.AUTH_DENIED;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.PERMISSION_DENIED;


public class PermissionDeniedException extends AuthException {

    public PermissionDeniedException() {
        super(PERMISSION_DENIED.getMessage(), AUTH_DENIED.getCode());
    }
}
