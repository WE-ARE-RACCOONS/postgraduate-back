package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.req.SendMessageRequest;
import com.postgraduate.admin.application.dto.res.UserInfo;
import com.postgraduate.admin.application.dto.res.WishResponse;
import com.postgraduate.domain.adminssr.application.dto.res.*;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import com.postgraduate.domain.wish.domain.service.WishUpdateService;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingSuccessRequest;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.admin.application.mapper.AdminMapper.mapToWishResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserUseCase {
    private final WishGetService wishGetService;
    private final WishUpdateService wishUpdateService;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;

    @Transactional(readOnly = true)
    public List<UserInfo> userInfos() {
        List<Wish> all = wishGetService.all();
        return all.stream()
                .map(AdminMapper::mapToUserInfo)
                .toList();
    }

    @Transactional(readOnly = true)
    public WishResponse wishInfo(Long userId) {
        Wish wish = wishGetService.byUserId(userId);
        return mapToWishResponse(wish);
    }

    public void wishDone(Long wishId) {
        Wish wish = wishGetService.byWishId(wishId);
        wishUpdateService.updateWishDone(wish);
    }

    public void sendMatchingMessage(SendMessageRequest request) {
        bizppurioJuniorMessage.matchingSuccess(new JuniorMatchingSuccessRequest(
                request.phoneNumber(), request.nickName(), request.postgraduate(), request.major())
        );
    }
}
