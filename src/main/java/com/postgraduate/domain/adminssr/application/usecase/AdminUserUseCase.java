package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.adminssr.application.dto.res.*;
import com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import com.postgraduate.domain.wish.domain.service.WishUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper.mapToWishResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserUseCase {
    private final WishGetService wishGetService;
    private final WishUpdateService wishUpdateService;

    public List<UserInfo> userInfos() {
        List<Wish> all = wishGetService.all();
        return all.stream()
                .map(AdminSsrMapper::mapToUserInfo)
                .toList();
    }

    public WishResponse wishInfo(Long userId) {
        Wish wish = wishGetService.byUserId(userId);
        return mapToWishResponse(wish);
    }

    public void wishDone(Long wishId) {
        Wish wish = wishGetService.byWishId(wishId);
        wishUpdateService.updateWishDone(wish);
    }
}
