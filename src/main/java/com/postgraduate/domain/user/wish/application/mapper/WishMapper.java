package com.postgraduate.domain.user.wish.application.mapper;

import com.postgraduate.global.auth.login.application.dto.req.SignUpRequest;
import com.postgraduate.global.auth.login.application.dto.req.UserChangeRequest;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.wish.domain.entity.Wish;
import com.postgraduate.domain.user.wish.domain.entity.constant.Status;

public class WishMapper {
    private WishMapper() {
        throw new IllegalArgumentException();
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

    public static Wish mapToWish(User user, UserChangeRequest request) {
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
