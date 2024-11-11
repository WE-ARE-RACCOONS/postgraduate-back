package com.postgraduate.domain.member.user.application.mapper;

import com.postgraduate.domain.member.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.member.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import com.postgraduate.domain.member.user.domain.entity.constant.Status;
import com.postgraduate.domain.member.user.domain.entity.Wish;
import com.postgraduate.global.auth.login.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.global.auth.login.application.dto.req.SignUpRequest;
import com.postgraduate.domain.member.user.domain.entity.User;

public class UserMapper {

    private UserMapper() {
        throw new IllegalArgumentException();
    }

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

    public static User mapToUser(SignUpRequest request, String profile) {
        return User.builder()
                .socialId(request.socialId())
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber())
                .marketingReceive(request.marketingReceive())
                .profile(profile)
                .build();
    }

    public static User mapToUser(SeniorSignUpRequest request, String profile) {
        return User.builder()
                .socialId(request.socialId())
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber())
                .marketingReceive(request.marketingReceive())
                .profile(profile)
                .role(Role.SENIOR)
                .build();
    }

    public static Wish mapToWish(User user, SignUpRequest request) {
        Status matchingStatus = request.matchingReceive() ? Status.WAITING : Status.REJECTED;
        return Wish.builder()
                .user(user)
                .major(request.major())
                .field(request.field())
                .matchingReceive(request.matchingReceive())
                .status(matchingStatus)
                .build();
    }
}
