package com.postgraduate.domain.wish.application.mapper;

import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.wish.application.dto.request.WishCreateRequest;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.entity.Wish.WishBuilder;

public class WishMapper {
    public static Wish mapToWish(WishCreateRequest request, User user) {
        WishBuilder wishBuilder = Wish.builder()
                .field(request.field())
                .postgradu(request.postgradu())
                .lab(request.lab());
        if (user == null) {
            return wishBuilder
                    .phoneNumber(request.phoneNumber())
                    .build();
        }
        return wishBuilder
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
