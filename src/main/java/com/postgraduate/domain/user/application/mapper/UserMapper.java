package com.postgraduate.domain.user.application.mapper;

import com.postgraduate.domain.auth.application.dto.SignUpRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.domain.entity.User;

public class UserMapper {
    public static UserInfoResponse mapToInfo(User user) {
        return UserInfoResponse.builder()
                .nickName(user.getNickName())
                .account(user.getAccount())
                .bank(user.getBank())
                .point(user.getPoint())
                .build();
    }

    public static User mapToUser(SignUpRequest request) {
        return User.builder()
                .socialId(request.getSocialId())
                .nickName(request.getNickName())
                .build();
    }
}
