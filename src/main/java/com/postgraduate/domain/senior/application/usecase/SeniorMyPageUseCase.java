package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class SeniorMyPageUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final SecurityUtils securityUtils;

    public SeniorInfoResponse seniorInfo(AuthDetails authDetails) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Boolean status = senior.getStatus();
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return SeniorMapper.mapToSeniorInfo(senior, status, profile.isPresent());
    }

    public void updateCertification(AuthDetails authDetails, SeniorCertificationRequest certificationRequest) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateCertification(senior, certificationRequest.getCertification());
    }

    public void updateProfile(AuthDetails authDetails, SeniorProfileRequest profileRequest) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateSeniorProfile(senior, profileRequest);
    }
}
