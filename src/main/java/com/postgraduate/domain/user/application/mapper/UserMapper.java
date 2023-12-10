package com.postgraduate.domain.user.application.mapper;

import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.user.domain.entity.User;

import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;

public class UserMapper {
    public static UserMyPageResponse mapToMyPageInfo(User user) {
        return new UserMyPageResponse(
                user.getNickName(),
                user.getProfile()
        );
    }

    public static UserInfoResponse mapToInfo(User user) {
        return new UserInfoResponse(
                user.getProfile(),
                user.getNickName(),
                user.getPhoneNumber()
        );
    }

    public static User mapToUser(SignUpRequest request) {
        return User.builder()
                .socialId(request.socialId())
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber())
                .marketingReceive(request.marketingReceive())
                .build();
    }

    public static User mapToUser(SeniorSignUpRequest request) {
        return User.builder()
                .socialId(request.socialId())
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber())
                .marketingReceive(request.marketingReceive())
                .role(SENIOR)
                .build();
    }
}
