package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.req.Login;
import com.postgraduate.admin.application.dto.res.UserInfos;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminUserService;
import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserUseCase {
    private final AdminUserService adminUserService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public User login(Login loginForm) {
        return adminUserService.login(loginForm.nickName(), loginForm.phoneNumber());
    }

    @Transactional(readOnly = true)
    public UserInfos userInfos(Integer page) {
        Page<MemberRole> all = adminUserService.allJunior(page);
        return new UserInfos(all.stream()
                .map(adminMapper::mapToUserInfo)
                .toList());
    }
}
