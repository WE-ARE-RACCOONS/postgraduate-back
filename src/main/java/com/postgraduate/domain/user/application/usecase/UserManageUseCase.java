package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.global.auth.login.application.dto.req.RejoinRequest;
import com.postgraduate.global.auth.login.application.usecase.oauth.SelectOauth;
import com.postgraduate.global.auth.login.application.usecase.oauth.SignOutUseCase;
import com.postgraduate.global.auth.login.presentation.constant.Provider;
import com.postgraduate.domain.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.exception.DeletedUserException;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserManageUseCase {
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;
    private final UserUtils userUtils;
    private final SelectOauth selectOauth;

    public void updateInfo(User user, UserInfoRequest userInfoRequest) {
        userUtils.checkPhoneNumber(userInfoRequest.phoneNumber());
        user = userGetService.byUserId(user.getUserId());
        userUpdateService.updateInfo(user, userInfoRequest);
    }

    public void tutorialFin(User user) {
        user = userGetService.byUserId(user.getUserId());
        userUpdateService.tutorialFin(user);
    }

    @Transactional(readOnly = true)
    public Boolean duplicatedNickName(String nickName) {
        return userGetService.byNickName(nickName).isEmpty();
    }

    public User updateRejoin(Provider provider, RejoinRequest request) {
        User user = userGetService.bySocialId(request.socialId());
        if (!user.isDelete())
            throw new UserNotFoundException();
        if (!request.rejoin() || user.isRealDelete()) {
            SignOutUseCase signOutUseCase = selectOauth.selectSignOut(provider);
            signOutUseCase.reSignOut(request.socialId());
            throw new DeletedUserException();
        }
        userUpdateService.updateRestore(user);
        return user;
    }
}
