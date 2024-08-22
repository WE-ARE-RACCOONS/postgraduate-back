package com.postgraduate.domain.auth.application.usecase.oauth;

import com.postgraduate.domain.auth.application.dto.req.SignOutRequest;
import com.postgraduate.domain.user.user.domain.entity.User;

public interface SignOutUseCase {
    void signOut(User user, SignOutRequest signOutRequest);
}
