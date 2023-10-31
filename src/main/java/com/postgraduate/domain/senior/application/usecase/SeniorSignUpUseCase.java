package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorSignUpUseCase {
    private final SeniorSaveService seniorSaveService;
    private final SecurityUtils securityUtils;

    public void signUp(AuthDetails authDetails, SeniorSignUpRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        seniorSaveService.saveSenior(user, request);
    }
}
