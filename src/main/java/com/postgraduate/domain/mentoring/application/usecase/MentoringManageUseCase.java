package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringDoneException;
import com.postgraduate.domain.mentoring.exception.MentoringNotExpectedException;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.application.mapper.RefuseMapper;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageUseCase {
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final MentoringUpdateService mentoringUpdateService;
    private final MentoringGetService mentoringGetService;
    private final RefuseSaveService refuseSaveService;
    private final SalarySaveService salarySaveService;
    private final AccountGetService accountGetService;
    private final SeniorGetService seniorGetService;

    public void updateCancel(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        mentoringUpdateService.updateStatus(mentoring, CANCEL);
    }

    public void updateDone(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (mentoring.getStatus() != EXPECTED)
            throw new MentoringNotExpectedException();
        createSalary(mentoring);
        mentoringUpdateService.updateStatus(mentoring, DONE);
    }

    private void createSalary(Mentoring mentoring) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        Salary salary = SalaryMapper.mapToSalary(mentoring, salaryDate);
        salarySaveService.saveSalary(salary);
    }

    public void updateRefuse(User user, Long mentoringId, MentoringRefuseRequest request) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(senior, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        Refuse refuse = RefuseMapper.mapToRefuse(mentoring, request);
        refuseSaveService.saveRefuse(refuse);
        mentoringUpdateService.updateStatus(mentoring, REFUSE);
    }

    public Boolean updateExpected(User user, Long mentoringId, MentoringDateRequest dateRequest) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(senior, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        mentoringUpdateService.updateDate(mentoring, dateRequest.getDate());
        mentoringUpdateService.updateStatus(mentoring, EXPECTED);
        Optional<Account> account = accountGetService.bySenior(senior);
        return account.isPresent();
    }

    @Scheduled(cron = "0 59 23 * * *", zone = "Asia/Seoul")
    public void updateCancel() {
        LocalDateTime now = LocalDateTime.now()
                .toLocalDate()
                .atStartOfDay();
        List<Mentoring> mentorings = mentoringGetService.byStatusAndCreatedAt(WAITING, now);
        mentorings.forEach(mentoring -> {
            mentoringUpdateService.updateStatus(mentoring, CANCEL);
            Refuse refuse = RefuseMapper.mapToRefuse(mentoring);
            refuseSaveService.saveRefuse(refuse);
            //TODO : 알림 보내거나 나머지 작업
        });
    }
}
