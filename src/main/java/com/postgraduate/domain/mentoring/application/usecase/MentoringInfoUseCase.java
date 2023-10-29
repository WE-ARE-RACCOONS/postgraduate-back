package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.MentoringInfo;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.global.auth.AuthDetails;
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
    private final UserRepository userRepository;

    public AppliedMentoringResponse getMentorings(Status status, AuthDetails authDetails) {
        Long userId = authDetails.getUserId();
        User user = userRepository.findById(userId).get();

        List<Mentoring> mentorings = mentoringGetService.mentoringByUser(user, status);
        return getCategories(status, mentorings);
    }

    private AppliedMentoringResponse getCategories(Status status, List<Mentoring> mentorings) {
        List<MentoringInfo> mentoringInfos = new ArrayList<>();

        switch (status) {
            case WAITING, DONE -> {
                for (Mentoring mentoring : mentorings) {
                    mentoringInfos.add(MentoringMapper.mapToWaitingOrDoneMentoringInfo(mentoring));
                }
                return new AppliedMentoringResponse(mentoringInfos);
            }

            default -> {
                for (Mentoring mentoring : mentorings) {
                    mentoringInfos.add(MentoringMapper.mapToExpectedInfo(mentoring));
                }
                return new AppliedMentoringResponse(mentoringInfos);
            }
        }
    }
}
