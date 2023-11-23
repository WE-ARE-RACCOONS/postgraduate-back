package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.res.SeniorDetailResponse;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorDetail;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorInfoUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;

    public SeniorDetailResponse getSeniorDetail(Long seniorId) {
        Senior senior = seniorGetService.bySeniorIdWithCertification(seniorId);
        seniorUpdateService.updateHit(senior);
        return mapToSeniorDetail(senior);
    }
}
