package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorSignUpUseCase {
    private final UserUpdateService userUpdateService;
    private final SeniorSaveService seniorSaveService;
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final SecurityUtils securityUtils;

    public void signUp(AuthDetails authDetails, SeniorSignUpRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        userUpdateService.updateRole(user.getUserId(), Role.SENIOR);
        Senior senior = SeniorMapper.mapToSenior(user, request);
        seniorSaveService.saveSenior(senior);
    }

    public void updateProfile(AuthDetails authDetails, SeniorProfileRequest profileRequest) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Profile profile = SeniorMapper.mapToProfile(profileRequest);
        seniorUpdateService.updateSeniorProfile(senior, profile);
    }
}
