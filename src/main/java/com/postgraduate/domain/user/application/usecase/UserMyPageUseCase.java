package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.mapper.UserMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserMyPageUseCase {
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;

    public UserInfoResponse getUserInfo(User user) {
        return UserMapper.mapToInfo(user);
    }

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
