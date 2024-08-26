package com.postgraduate.global.auth.login.application.usecase.oauth;

import com.postgraduate.global.auth.login.application.dto.req.CodeRequest;
import com.postgraduate.global.auth.login.application.dto.req.TokenRequest;
import com.postgraduate.global.auth.login.application.dto.res.AuthUserResponse;

public interface SignInUseCase {
    AuthUserResponse getUser(CodeRequest request);

    AuthUserResponse getDevUser(CodeRequest codeRequest);

    AuthUserResponse getUserWithToken(TokenRequest request);
}