package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.application.mapper.RefuseMapper;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageUseCase {
    private static final int SALARY_DATE = 10;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final MentoringUpdateService mentoringUpdateService;
    private final RefuseSaveService refuseSaveService;
    private final SalarySaveService salarySaveService;

    public void updateCancel(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, CANCEL);
    }

    public void updateDone(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        createSalary(mentoring);
        mentoringUpdateService.updateStatus(mentoring, DONE);
    }

    private void createSalary(Mentoring mentoring) {
        LocalDate now = LocalDate.now();
        LocalDate salaryDate = now.getDayOfMonth() < SALARY_DATE
                ? now.withDayOfMonth(SALARY_DATE)
                : now.plusMonths(1).withDayOfMonth(SALARY_DATE);
        Salary salary = SalaryMapper.mapToSalary(mentoring, salaryDate);
        salarySaveService.saveSalary(salary);
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
}
