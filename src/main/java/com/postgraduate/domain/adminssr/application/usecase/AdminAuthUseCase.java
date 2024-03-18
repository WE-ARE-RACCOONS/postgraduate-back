package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.adminssr.application.dto.req.Login;
import com.postgraduate.domain.adminssr.domain.service.AuthGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminAuthUseCase {
    private final AuthGetService authGetService;

    public User login(Login loginForm) {
        return authGetService.login(loginForm.nickName(), loginForm.phoneNumber());
    }

}
