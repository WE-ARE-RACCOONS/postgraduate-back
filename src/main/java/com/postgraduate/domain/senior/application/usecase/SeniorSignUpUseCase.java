package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.global.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorSignUpUseCase {
    private final SeniorSaveService seniorSaveService;
    private final UserGetService userGetService;

    public void signUp(AuthDetails authDetails, SeniorSignUpRequest request) {
        User user = userGetService.getUser(authDetails.getUserId());
        seniorSaveService.saveSenior(user, request);
    }
}
