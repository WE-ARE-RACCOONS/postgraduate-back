package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.mapper.UserMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserMyPageUseCase {
    private final UserGetService userGetService;

    public UserInfoResponse getUserInfo() {
        Long userId = 1l;
        /**
         * securityUtils 만들어서 사용
         */
        User user = userGetService.getUser(userId);
        return UserMapper.mapToInfo(user);
    }
}
