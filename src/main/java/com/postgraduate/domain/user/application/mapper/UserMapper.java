package com.postgraduate.domain.user.application.mapper;

import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;

public class UserMapper {
    public static UserInfoResponse mapToInfo(User user) {
        return UserInfoResponse.builder()
                .nickName(user.getNickName())
                .account(user.getAccount())
                .bank(user.getBank())
                .point(user.getPoint())
                .build();
    }

    public static User mapToUser(Long socialId) {
        return User.builder()
                .socialId(socialId)
                .role(Role.USER)
                .build();
    }
}
