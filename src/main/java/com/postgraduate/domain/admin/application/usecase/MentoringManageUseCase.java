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
public class MentoringManageUseCase {
    private final MentoringGetService mentoringGetService;
    public List<MentoringResponse> getMentorings() {
        List<Mentoring> mentorings = mentoringGetService.all();
        return mentorings.stream().map(AdminMapper::mapToMentoringResponse).toList();
    }
}
