package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.application.mapper.RefuseMapper;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static com.postgraduate.domain.salary.util.MonthFormat.getMonthFormat;
import static java.time.LocalDate.now;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageUseCase {
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final MentoringUpdateService mentoringUpdateService;
    private final MentoringGetService mentoringGetService;
    private final RefuseSaveService refuseSaveService;
    private final SalaryGetService salaryGetService;
    private final SalarySaveService salarySaveService;
    private final SalaryUpdateService salaryUpdateService;

    public void updateCancel(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, CANCEL);
    }

    public void updateDone(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);

        Salary salary = salaryGetService.bySeniorAndMonth(mentoring.getSenior(), now().format(getMonthFormat()))
                .orElse(
                        salarySaveService.saveSalary(
                                SalaryMapper.mapToSalary(
                                        now().format(getMonthFormat()),
                                        mentoring.getSenior())
                        ));

        salaryUpdateService.updateAmount(salary, mentoring);
        mentoringUpdateService.updateStatus(mentoring, DONE);
    }

    public void updateRefuse(User user, Long mentoringId, MentoringRefuseRequest request) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(user, mentoringId);
        Refuse refuse = RefuseMapper.mapToRefuse(mentoring, request);
        refuseSaveService.saveRefuse(refuse);
        mentoringUpdateService.updateStatus(mentoring, REFUSE);
    }

    public void updateExpected(User user, Long mentoringId, MentoringDateRequest dateRequest) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(user, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        mentoringUpdateService.updateDate(mentoring, dateRequest.getDate());
        mentoringUpdateService.updateStatus(mentoring, EXPECTED);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    private void updateCancel() {
        LocalDate now = LocalDate.now();
        List<Mentoring> mentorings = mentoringGetService.byStatusAndCreatedAt(WAITING, now);
        for (Mentoring mentoring : mentorings) {
            mentoringUpdateService.updateStatus(mentoring, CANCEL);
            //TODO : 알림 보내거나 나머지 작업
        }
    }
}
