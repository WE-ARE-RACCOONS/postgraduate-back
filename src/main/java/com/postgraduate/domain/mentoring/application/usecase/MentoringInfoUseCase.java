package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringInfo;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringInfoUseCase {
    private final MentoringGetService mentoringGetService;

    /**
     * securityUtils 이후 수정
     */
    private final SecurityUtils securityUtils;

    public AppliedMentoringResponse getMentorings(Status status, AuthDetails authDetails) {
        User user = securityUtils.getLoggedInUser(authDetails);

        List<Mentoring> mentorings = mentoringGetService.mentoringByUser(user, status);
        return getCategories(status, mentorings);
    }

    private AppliedMentoringResponse getCategories(Status status, List<Mentoring> mentorings) {
        List<AppliedMentoringInfo> appliedMentoringInfos = new ArrayList<>();

        switch (status) {
            case WAITING, DONE -> {
                for (Mentoring mentoring : mentorings) {
                    appliedMentoringInfos.add(MentoringMapper.mapToWaitingOrDoneAppliedInfo(mentoring));
                }
                return new AppliedMentoringResponse(appliedMentoringInfos);
            }

            default -> {
                for (Mentoring mentoring : mentorings) {
                    appliedMentoringInfos.add(MentoringMapper.mapToExpectedAppliedInfo(mentoring));
                }
                return new AppliedMentoringResponse(appliedMentoringInfos);
            }
        }
    }

    public AppliedMentoringDetailResponse getMentoringDetail(Long mentoringId) {
        Mentoring mentoring = mentoringGetService.mentoringDetail(mentoringId);
        AppliedMentoringDetailResponse appliedMentoringDetailResponse = MentoringMapper.mapToAppliedDetailInfo(mentoring);
        return appliedMentoringDetailResponse;
    }
}
