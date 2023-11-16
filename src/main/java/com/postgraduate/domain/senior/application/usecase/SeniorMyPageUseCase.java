package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
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

    public SeniorInfoResponse seniorInfo(User user) {
        Senior senior = seniorGetService.byUser(user);
        Status status = senior.getStatus();
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return SeniorMapper.mapToSeniorInfo(senior, status, profile.isPresent());
    }

    public void updateCertification(User user, SeniorCertificationRequest certificationRequest) {
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateCertification(senior, certificationRequest.getCertification());
    }

    public void updateProfile(User user, SeniorProfileRequest profileRequest) {
        Senior senior = seniorGetService.byUser(user);
        Profile profile = SeniorMapper.mapToProfile(profileRequest);
        seniorUpdateService.updateSeniorProfile(senior, profile);
    }
}
