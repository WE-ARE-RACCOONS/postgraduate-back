package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringApplyUseCase {
    private final MentoringSaveService mentoringSaveService;
    private final SeniorGetService seniorGetService;

    public void applyMentoring(User user, MentoringApplyRequest request) {
        String[] dates = request.date().split(",");
        if (dates.length != 3)
            throw new MentoringDateException();
        Senior senior = seniorGetService.bySeniorId(request.seniorId());
        Mentoring mentoring = MentoringMapper.mapToMentoring(user, senior, request);
        mentoringSaveService.save(mentoring);
    }
}
