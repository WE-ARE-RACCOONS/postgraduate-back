package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringApplyUseCase {
    private final SecurityUtils securityUtils;
    private final MentoringSaveService mentoringSaveService;
    private final SeniorGetService seniorGetService;

    public void applyMentoring(AuthDetails authDetails, MentoringApplyRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.bySeniorId(request.getSeniorId());
        Mentoring mentoring = MentoringMapper.mapToMentoring(user, senior, request);
        mentoringSaveService.save(mentoring);
    }
}