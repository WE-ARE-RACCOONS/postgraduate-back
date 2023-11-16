package com.postgraduate.domain.user.application.mapper;

import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;

import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;

public class UserMapper {
    public static UserInfoResponse mapToInfo(User user) {
        return UserInfoResponse.builder()
                .nickName(user.getNickName())
                .profile(user.getProfile())
                .build();
    }

    public static User mapToUser(SignUpRequest request) {
        return User.builder()
                .socialId(request.getSocialId())
                .nickName(request.getNickName())
                .phoneNumber(request.getPhoneNumber())
                .marketingReceive(request.getMarketingReceive())
                .build();
    }

    public static User mapToUser(SeniorSignUpRequest request) {
        return User.builder()
                .socialId(request.getSocialId())
                .nickName(request.getNickName())
                .phoneNumber(request.getPhoneNumber())
                .marketingReceive(request.getMarketingReceive())
                .role(SENIOR)
                .build();
    }
}
