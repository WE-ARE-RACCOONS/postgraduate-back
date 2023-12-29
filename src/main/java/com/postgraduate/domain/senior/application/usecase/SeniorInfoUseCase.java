package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;
import com.postgraduate.domain.available.application.dto.res.AvailableTimesResponse;
import com.postgraduate.domain.available.application.mapper.AvailableMapper;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.service.AvailableGetService;
import com.postgraduate.domain.senior.application.dto.res.AllSeniorSearchResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorDetailResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorSearchResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorDetail;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorProfile;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorInfoUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final AvailableGetService availableGetService;

    public SeniorDetailResponse getSeniorDetail(Long seniorId) {
        Senior senior = seniorGetService.bySeniorIdWithCertification(seniorId);
        seniorUpdateService.updateHit(senior);
        List<Available> availables = availableGetService.bySenior(senior);
        List<AvailableTimeResponse> times = availables.stream()
                .map(AvailableMapper::mapToAvailableTimes)
                .toList();
        return mapToSeniorDetail(senior, times);
    }

    public AllSeniorSearchResponse getSearchSenior(String search, Integer page, String sort) {
        Page<Senior> seniors = seniorGetService.bySearch(search, page, sort);
        List<SeniorSearchResponse> selectSeniors = seniors.stream()
                .map(SeniorMapper::mapToSeniorSearch)
                .toList();
        long totalElements = seniors.getTotalElements();
        return new AllSeniorSearchResponse(selectSeniors, totalElements);
    }

    public AllSeniorSearchResponse getFieldSenior(String field, String postgradu, Integer page) {
        Page<Senior> seniors = seniorGetService.byField(field, postgradu, page);
        List<SeniorSearchResponse> selectSeniors = seniors.stream()
                .map(SeniorMapper::mapToSeniorSearch)
                .toList();
        long totalElements = seniors.getTotalElements();
        return new AllSeniorSearchResponse(selectSeniors, totalElements);
    }

    public SeniorProfileResponse getSeniorProfile(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        SeniorProfileResponse seniorProfileResponse = mapToSeniorProfile(senior);
        return seniorProfileResponse;
    }

    public AvailableTimesResponse getSeniorTimes(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        List<Available> availables = availableGetService.bySenior(senior);
        List<AvailableTimeResponse> times = availables.stream()
                .map(AvailableMapper::mapToAvailableTimes)
                .toList();
        return new AvailableTimesResponse(times);
    }
}
