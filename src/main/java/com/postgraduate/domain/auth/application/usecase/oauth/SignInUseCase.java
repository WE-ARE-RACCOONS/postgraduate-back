package com.postgraduate.domain.auth.application.usecase.oauth;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.req.TokenRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;

public interface SignInUseCase {
    AuthUserResponse getUser(CodeRequest request);

    AuthUserResponse getDevUser(CodeRequest codeRequest);

    AuthUserResponse getUserWithToken(TokenRequest request);
}