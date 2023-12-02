package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.res.MentoringResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageByAdminUseCase {
    private final MentoringGetService mentoringGetService;
    public List<MentoringResponse> getUserMentorings(Long userId) {
        List<Mentoring> mentorings = mentoringGetService.byUserId(userId);
        return mentorings.stream().map(AdminMapper::mapToMentoringResponse).toList();
    }
    public List<MentoringResponse> getSeniorMentorings(Long seniorId) {
        List<Mentoring> mentorings = mentoringGetService.bySeniorId(seniorId);
        return mentorings.stream().map(AdminMapper::mapToMentoringResponse).toList();
    }
}
