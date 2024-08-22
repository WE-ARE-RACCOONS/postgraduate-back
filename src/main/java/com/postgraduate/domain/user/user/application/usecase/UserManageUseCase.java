package com.postgraduate.domain.user.user.application.usecase;

import com.postgraduate.domain.user.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.user.application.utils.UserUtils;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.service.UserGetService;
import com.postgraduate.domain.user.user.domain.service.UserUpdateService;
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
}
