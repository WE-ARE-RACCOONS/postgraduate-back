package com.postgraduate.global.auth.login.application.usecase.oauth;

import com.postgraduate.global.auth.login.application.dto.req.SignOutRequest;
import com.postgraduate.domain.member.user.domain.entity.User;

public interface SignOutUseCase {
    void signOut(User user, SignOutRequest signOutRequest);

    void reSignOut(Long socialId);
}
