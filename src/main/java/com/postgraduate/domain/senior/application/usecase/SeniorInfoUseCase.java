package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.res.SeniorDetailResponse;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorDetail;

@Service
@RequiredArgsConstructor
public class SeniorInfoUseCase {
    private final SeniorGetService seniorGetService;

    public SeniorDetailResponse getSeniorDetail(Long seniorId) {
        Senior senior = seniorGetService.bySeniorIdWithProfile(seniorId);
        return mapToSeniorDetail(senior);
    }
}
