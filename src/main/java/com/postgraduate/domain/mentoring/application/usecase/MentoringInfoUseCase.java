package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDoneException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final SeniorGetService seniorGetService;
    private final SecurityUtils securityUtils;
    /**
     * securityUtils 이후 수정
     */

    public AppliedMentoringResponse getMentorings(Status status, AuthDetails authDetails) {
        User user = securityUtils.getLoggedInUser(authDetails);

        List<Mentoring> mentorings = mentoringGetService.mentoringByUser(user, status);
        return getCategories(status, mentorings);
    }

    public List<SeniorMentoringResponse> getSeniorMentorings(Status status, AuthDetails authDetails) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);

        List<Mentoring> mentorings = mentoringGetService.mentoringBySenior(senior, status);
        return mentorings.stream().map(MentoringMapper::mapToSeniorMentoring).collect(Collectors.toList());
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

    public AppliedMentoringDetailResponse getMentoringDetail(AuthDetails authDetails, Long mentoringId) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(user, mentoringId);
        return MentoringMapper.mapToAppliedDetailInfo(mentoring);
    }

    public SeniorMentoringDetailResponse getSeniorMentoringDetail(AuthDetails authDetails, Long mentoringId) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(senior, mentoringId);
        if (mentoring.getStatus() == DONE) {
            throw new MentoringDoneException();
        }
        return MentoringMapper.mapToSeniorMentoringDetail(mentoring);
    }
}
