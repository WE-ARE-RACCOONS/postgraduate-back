package com.postgraduate.domain.member.senior.application.usecase;

import com.postgraduate.domain.member.senior.application.dto.res.*;
import com.postgraduate.domain.member.senior.domain.entity.Available;
import com.postgraduate.domain.member.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.member.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.member.senior.application.mapper.SeniorMapper.*;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorInfoUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;

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
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.updateHit(senior);
        List<Available> availables = senior.getAvailables();
        List<AvailableTimeResponse> times = availables.stream()
                .map(SeniorMapper::mapToAvailableTimes)
                .toList();
        return mapToSeniorDetail(senior, times, isMine);
    }

    private SeniorDetailResponse getResponseMine(Senior senior, boolean isMine) {
        List<Available> availables = senior.getAvailables();
        List<AvailableTimeResponse> times = availables.stream()
                .map(SeniorMapper::mapToAvailableTimes)
                .toList();
        return mapToSeniorDetail(senior, times, isMine);
    }

    @Transactional(readOnly = true)
    public AllSeniorSearchResponse getSearchSenior(String search, Integer page, String sort) {
        Page<Senior> seniors = seniorGetService.bySearch(search, page, sort);
        List<SeniorSearchResponse> selectSeniors = seniors.stream()
                .map(SeniorMapper::mapToSeniorSearchWithStatus)
                .toList();
        long totalElements = seniors.getTotalElements();
        return new AllSeniorSearchResponse(selectSeniors, totalElements);
    }

    @Transactional(readOnly = true)
    public AllSeniorSearchResponse getFieldSenior(String field, String postgradu, Integer page) {
        Page<Senior> seniors = seniorGetService.byField(field, postgradu, page);
        List<SeniorSearchResponse> selectSeniors = seniors.stream()
                .map(SeniorMapper::mapToSeniorSearchWithStatus)
                .toList();
        long totalElements = seniors.getTotalElements();
        return new AllSeniorSearchResponse(selectSeniors, totalElements);
    }

    @Transactional(readOnly = true)
    public SeniorProfileResponse getSeniorProfile(User user, Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        return mapToSeniorProfile(user, senior);
    }

    @Transactional(readOnly = true)
    public AvailableTimesResponse getSeniorTimes(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        List<Available> availables = senior.getAvailables();
        List<AvailableTimeResponse> times = availables.stream()
                .map(SeniorMapper::mapToAvailableTimes)
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
