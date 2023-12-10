package com.postgraduate.domain.wish.application.mapper;

import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;

public class WishMapper {
    public static Wish mapToWish(User user, SignUpRequest request) {
        return Wish.builder()
                .user(user)
                .major(request.major())
                .field(request.field())
                .matchingReceive(request.matchingReceive())
                .build();
    }
}
