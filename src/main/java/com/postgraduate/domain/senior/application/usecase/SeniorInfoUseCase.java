package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.res.AllSeniorSearchResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorDetailResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorSearchResponse;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorDetail;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorSearch;

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

    public AllSeniorSearchResponse getSearchSenior(String search, Integer page, String sort) {
        Page<Senior> allSeniors = seniorGetService.bySearch(search, page, sort);
        List<SeniorSearchResponse> selectSeniors = new ArrayList<>();
        for (Senior senior : allSeniors.getContent()) {
            selectSeniors.add(mapToSeniorSearch(senior));
        }
        return new AllSeniorSearchResponse(selectSeniors);
    }

    public AllSeniorSearchResponse getFieldSenior(String field, String postgradu, Integer page) {
        Page<Senior> allSeniors = seniorGetService.byField(field, postgradu, page);
        List<SeniorSearchResponse> selectSeniors = new ArrayList<>();
        for (Senior senior : allSeniors.getContent()) {
            selectSeniors.add(mapToSeniorSearch(senior));
        }
        return new AllSeniorSearchResponse(selectSeniors);
    }
}
