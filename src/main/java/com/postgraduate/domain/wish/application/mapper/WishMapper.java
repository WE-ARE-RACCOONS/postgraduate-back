package com.postgraduate.domain.wish.application.mapper;

import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.application.mapper.dto.res.WishResponse;
import com.postgraduate.domain.wish.domain.entity.Wish;

public class WishMapper {
    public static Wish mapToWish(User user, SignUpRequest request) {
        return Wish.builder()
                .user(user)
                .major(request.getMajor())
                .field(request.getField())
                .matchingReceive(request.getMatchingReceive())
                .build();
    }

    public static WishResponse mapToWish(Wish wish) {
        User user = wish.getUser();
        String[] field = wish.getField().split(",");
        return new WishResponse(user.getNickName(), user.getPhoneNumber(), user.getCreatedAt(), user.getMarketingReceive(),
                wish.getMatchingReceive(), wish.getMajor(), field);
    }
}
