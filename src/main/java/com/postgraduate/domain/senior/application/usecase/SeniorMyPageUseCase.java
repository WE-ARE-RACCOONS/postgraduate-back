package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.salary.application.dto.res.SalaryDetailResponse;
import com.postgraduate.domain.salary.application.dto.res.SalaryInfoResponse;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional
public class SeniorMyPageUseCase {
    private static final int SALARY_DATE = 10;
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final UserUpdateService userUpdateService;

    public SeniorInfoResponse seniorInfo(User user) {
        Senior senior = seniorGetService.byUser(user);
        Status status = senior.getStatus();
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return SeniorMapper.mapToSeniorInfo(senior, status, profile.isPresent());
    }

    public void updateSeniorMyPageProfile(User user, SeniorMyPageProfileRequest myPageProfileRequest) {
        userUpdateService.updateSeniorMyPage(user.getUserId(), myPageProfileRequest);
        Senior senior = seniorGetService.byUser(user);
        seniorUpdateService.updateMyPageProfile(senior, myPageProfileRequest);
    }

    public SalaryInfoResponse getSalary(User user) {
        Senior senior = seniorGetService.byUser(user);
        LocalDate settlementDate = getSettlementDate();
        List<Salary> salaries = salaryGetService.bySeniorAndSalaryDate(senior, settlementDate);
        int pay = getAmount(salaries);
        return new SalaryInfoResponse(settlementDate, pay); //TODO 수수료
    }

    private LocalDate getSettlementDate() {
        LocalDate now = LocalDate.now();
        return now.getDayOfMonth() <= SALARY_DATE
                ? now.withDayOfMonth(SALARY_DATE)
                : now.plusMonths(1).withDayOfMonth(SALARY_DATE);
    }

    private int getAmount(List<Salary> salaries) {
        return salaries.stream()
                .map(salary -> salary.getMentoring().getPay())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public List<SalaryDetailResponse> getSalaryDetail(User user, Boolean status) {
        Senior senior = seniorGetService.byUser(user);
        List<Salary> salaries = salaryGetService.bySeniorAndStatus(senior, status);
        return salaries.stream().map(SalaryMapper::mapToSalaryDetail).toList();
    }
}
