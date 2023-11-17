package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserManageUseCase {
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;

    public void updateNickName(User user, String nickName) {
        userUpdateService.updateNickName(user.getUserId(), nickName);
    }

    public void updateProfile(User user, String profile) {
        userUpdateService.updateProfile(user.getUserId(), profile);
    }

    public boolean duplicatedNickName(String nickName) {
        return userGetService.byNickName(nickName).isEmpty();
    }
}
