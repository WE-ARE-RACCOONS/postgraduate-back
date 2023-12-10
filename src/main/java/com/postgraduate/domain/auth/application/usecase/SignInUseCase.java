package com.postgraduate.domain.auth.application.usecase;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;

public interface SignInUseCase {
    AuthUserResponse getUser(CodeRequest request);
}
