package com.postgraduate.global.config.security.jwt.exception;

import com.postgraduate.global.config.security.jwt.constant.JwtResponseCode;
import com.postgraduate.global.config.security.jwt.constant.JwtResponseMessage;

public class DeletedUserException extends JwtException{
    public DeletedUserException() {
        super(JwtResponseMessage.DELETE_USER.getMessage(), JwtResponseCode.DELETE_USER.getCode());
    }
}
