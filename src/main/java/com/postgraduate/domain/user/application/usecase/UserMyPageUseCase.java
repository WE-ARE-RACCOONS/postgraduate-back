package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.mapper.UserMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserMyPageUseCase {
    private final SecurityUtils securityUtils;
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;

    public UserInfoResponse getUserInfo(AuthDetails authDetails) {
        User user = securityUtils.getLoggedInUser(authDetails);
        return UserMapper.mapToInfo(user);
    }

    public void updateNickName(AuthDetails authDetails, String nickName) {
        User user = securityUtils.getLoggedInUser(authDetails);
        userUpdateService.updateNickName(user.getUserId(), nickName);
    }

    public void updateProfile(AuthDetails authDetails, String profile) {
        User user = securityUtils.getLoggedInUser(authDetails);
        userUpdateService.updateProfile(user.getUserId(), profile);
    }

    public boolean duplicatedNickName(String nickName) {
        return userGetService.byNickName(nickName).isEmpty();
    }
}
