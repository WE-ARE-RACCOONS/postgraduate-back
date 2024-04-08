package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;
import com.postgraduate.domain.available.application.dto.res.AvailableTimesResponse;
import com.postgraduate.domain.available.application.mapper.AvailableMapper;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.service.AvailableGetService;
import com.postgraduate.domain.senior.application.dto.res.*;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorDetail;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorProfile;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorInfoUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final AvailableGetService availableGetService;

    public SeniorDetailResponse getSeniorDetail(User user, Long seniorId) {
        if (user != null && user.getRole() == SENIOR)
            return checkIsMine(user, seniorId);
        return getResponse(seniorId, false);
    }

    private SeniorDetailResponse checkIsMine(User user, Long seniorId) {
        Senior mySenior = seniorGetService.byUser(user);
        if (mySenior.getSeniorId().equals(seniorId))
            return getResponseMine(mySenior, true);
        return getResponse(seniorId, false);
    }

    private SeniorDetailResponse getResponse(Long seniorId, boolean isMine) {
        Senior senior = seniorGetService.bySeniorIdWithCertification(seniorId);
        seniorUpdateService.updateHit(senior);
        List<Available> availables = availableGetService.bySenior(senior);
        List<AvailableTimeResponse> times = availables.stream()
                .map(AvailableMapper::mapToAvailableTimes)
                .toList();
        return mapToSeniorDetail(senior, times, isMine);
    }

    private SeniorDetailResponse getResponseMine(Senior senior, boolean isMine) {
        List<Available> availables = availableGetService.byMine(senior);
        List<AvailableTimeResponse> times = availables.stream()
                .map(AvailableMapper::mapToAvailableTimes)
                .toList();
        return mapToSeniorDetail(senior, times, isMine);
    }

    @Transactional(readOnly = true)
    public AllSeniorSearchResponse getSearchSenior(String search, Integer page, String sort) {
        Page<Senior> seniors = seniorGetService.bySearch(search, page, sort);
        List<SeniorSearchResponse> selectSeniors = seniors.stream()
                .map(SeniorMapper::mapToSeniorSearch)
                .toList();
        long totalElements = seniors.getTotalElements();
        return new AllSeniorSearchResponse(selectSeniors, totalElements);
    }

    @Transactional(readOnly = true)
    public AllSeniorSearchResponse getFieldSenior(String field, String postgradu, Integer page) {
        Page<Senior> seniors = seniorGetService.byField(field, postgradu, page);
        List<SeniorSearchResponse> selectSeniors = seniors.stream()
                .map(SeniorMapper::mapToSeniorSearch)
                .toList();
        long totalElements = seniors.getTotalElements();
        return new AllSeniorSearchResponse(selectSeniors, totalElements);
    }

    @Transactional(readOnly = true)
    public SeniorProfileResponse getSeniorProfile(User user, Long seniorId) {
        Senior senior = seniorGetService.bySeniorIdWithCertification(seniorId);
        return mapToSeniorProfile(user, senior);
    }

    @Transactional(readOnly = true)
    public AvailableTimesResponse getSeniorTimes(Long seniorId) {
        Senior senior = seniorGetService.bySeniorIdWithCertification(seniorId);
        List<Available> availables = availableGetService.bySenior(senior);
        List<AvailableTimeResponse> times = availables.stream()
                .map(AvailableMapper::mapToAvailableTimes)
                .toList();
        return new AvailableTimesResponse(senior.getUser().getNickName(), times);
    }

    @Transactional(readOnly = true)
    public AllSeniorIdResponse getAllSeniorId() {
        List<Senior> seniors = seniorGetService.allSeniorId();
        List<Long> seniorIds = seniors.stream()
                .map(Senior::getSeniorId)
                .toList();
        return new AllSeniorIdResponse(seniorIds);
    }
}
