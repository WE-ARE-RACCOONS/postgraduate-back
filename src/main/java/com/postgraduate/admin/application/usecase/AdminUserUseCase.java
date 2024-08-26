package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.req.Login;
import com.postgraduate.admin.application.dto.req.SendMessageRequest;
import com.postgraduate.admin.application.dto.res.UserInfo;
import com.postgraduate.admin.application.dto.res.WishResponse;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminUserService;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.wish.domain.entity.Wish;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingSuccessRequest;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserUseCase {
    private final BizppurioJuniorMessage bizppurioJuniorMessage;
    private final AdminUserService adminUserService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public User login(Login loginForm) {
        return adminUserService.login(loginForm.nickName(), loginForm.phoneNumber());
    }

    @Transactional(readOnly = true)
    public List<UserInfo> userInfos() {
        List<Wish> all = adminUserService.allJunior();
        return all.stream()
                .map(adminMapper::mapToUserInfo)
                .toList();
    }

    @Transactional(readOnly = true)
    public WishResponse wishInfo(Long userId) {
        Wish wish = adminUserService.wishByUserId(userId);
        return adminMapper.mapToWishResponse(wish);
    }

    public void wishDone(Long wishId) {
        adminUserService.updateWishDone(wishId);
    }

    public void sendMatchingMessage(SendMessageRequest request) {
        bizppurioJuniorMessage.matchingSuccess(new JuniorMatchingSuccessRequest(
                request.phoneNumber(), request.nickName(), request.postgraduate(), request.major())
        );
    }
}
