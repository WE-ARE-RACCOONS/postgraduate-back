package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.req.Login;
import com.postgraduate.admin.application.dto.req.SendMessageRequest;
import com.postgraduate.admin.application.dto.res.UserInfos;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminUserService;
import com.postgraduate.domain.member.user.domain.entity.User;
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
    public UserInfos userInfos() {
        List<User> all = adminUserService.allJunior();
        return new UserInfos(all.stream()
                .map(adminMapper::mapToUserInfo)
                .toList());
    }

    public void sendMatchingMessage(SendMessageRequest request) {
        bizppurioJuniorMessage.matchingSuccess(new JuniorMatchingSuccessRequest(
                request.phoneNumber(), request.nickName(), request.postgraduate(), request.major())
        );
    }
}
