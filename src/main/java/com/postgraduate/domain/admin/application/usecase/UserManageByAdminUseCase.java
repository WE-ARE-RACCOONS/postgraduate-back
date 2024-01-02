package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.UserInfo;
import com.postgraduate.domain.admin.application.dto.res.UserManageResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.wish.application.dto.res.WishResponse;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import com.postgraduate.domain.wish.domain.service.WishUpdateService;
import com.postgraduate.domain.wish.exception.MatchingNotReceiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserManageByAdminUseCase {
    private final WishGetService wishGetService;
    private final WishUpdateService wishUpdateService;

    public UserManageResponse getUsers(Integer page, String search) {
        Page<Wish> wishes = wishGetService.all(page, search);
        List<UserInfo> userInfos = wishes.stream()
                .map(AdminMapper::mapToUserInfo)
                .toList();
        long totalElements = wishes.getTotalElements();
        int totalPages = wishes.getTotalPages();
        return new UserManageResponse(userInfos, totalElements, totalPages);
    }

    public WishResponse getWish(Long wishId) {
        Wish wish = wishGetService.byWishId(wishId);
        return AdminMapper.mapToWishResponse(wish);
    }

    public void updateWishStatus(Long wishId) {
        Wish wish = wishGetService.byWishId(wishId);
        if (!wish.getMatchingReceive()) {
            throw new MatchingNotReceiveException();
        }
        wishUpdateService.updateWishStatus(wish);
    }
}
