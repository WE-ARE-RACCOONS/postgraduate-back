package com.postgraduate.domain.user.application.mapper;

import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.user.application.dto.req.UserHopeRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.domain.entity.Hope;
import com.postgraduate.domain.user.domain.entity.User;

public class UserMapper {
    public static UserInfoResponse mapToInfo(User user) {
        return UserInfoResponse.builder()
                .nickName(user.getNickName())
                .profile(user.getProfile())
                .point(user.getPoint())
                .build();
    }

    public static User mapToUser(SignUpRequest request) {
        Hope hope = Hope.builder()
                .field(request.getField())
                .major(request.getMajor())
                .receive(request.getReceive())
                .build();
        return User.builder()
                .socialId(request.getSocialId())
                .nickName(request.getNickName())
                .phoneNumber(request.getPhoneNumber())
                .hope(hope)
                .build();
    }

    public static Hope mapToHope(UserHopeRequest hopeRequest) {
        return Hope.builder()
                .major(hopeRequest.getMajor())
                .field(hopeRequest.getField())
                .receive(hopeRequest.isReceive())
                .build();
    }
}
