package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
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
    private final UserUpdateService userUpdateService;

    public SeniorInfoResponse seniorInfo(User user) {
        Senior senior = seniorGetService.byUser(user);
        Status status = senior.getStatus();
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return SeniorMapper.mapToSeniorInfo(senior, status, profile.isPresent());
    }

    public void updateSeniorMyPageProfile(User user, SeniorMyPageProfileRequest myPageProfileRequest) {
        userUpdateService.updateSeniorMyPage(user.getUserId(), myPageProfileRequest);
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateMyPageProfile(senior, myPageProfileRequest);
    }
}
