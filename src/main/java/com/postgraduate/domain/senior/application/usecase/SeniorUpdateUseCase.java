package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorUpdateUseCase {
    private final SeniorSaveService seniorSaveService;
    private final SeniorGetService seniorGetService;
    private final UserGetService userGetService;

    public void updateProfile(Long userId, SeniorProfileRequest request) {
        User user = userGetService.getUser(userId);
        Senior senior = seniorGetService.byUser(user);
        seniorSaveService.saveSenior(senior, request);
    }
}
