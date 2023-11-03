package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileAndAccountPageRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional
public class SeniorMyPageUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final SecurityUtils securityUtils;

    public SeniorInfoResponse seniorInfo(AuthDetails authDetails) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Status status = senior.getStatus();
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return SeniorMapper.mapToSeniorInfo(senior, status, profile.isPresent());
    }

    public void updateCertification(AuthDetails authDetails, SeniorCertificationRequest certificationRequest) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateCertification(senior, certificationRequest.getCertification());
    }

    public void updateProfile(AuthDetails authDetails, SeniorProfileAndAccountPageRequest profileRequest) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Profile profile = SeniorMapper.mapToProfile(profileRequest);
        Account account = SeniorMapper.mapToAccount(profileRequest);
        seniorUpdateService.updateSeniorProfileAndAccount(senior, profile, account);
    }

    public SeniorProfileResponse getSeniorProfile(AuthDetails authDetails) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        SeniorProfileResponse seniorProfileResponse = SeniorMapper.mapToSeniorProfileInfo(senior);
        return seniorProfileResponse;
    }
}
