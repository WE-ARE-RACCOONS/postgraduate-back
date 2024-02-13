package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringDeleteService;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
import com.postgraduate.domain.mentoring.exception.MentoringNotExpectedException;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.application.mapper.RefuseMapper;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MentoringDeleteService mentoringDeleteService;
    private final RefuseSaveService refuseSaveService;
    private final AccountGetService accountGetService;
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final MentoringSaveService mentoringSaveService;
    private final PaymentGetService paymentGetService;

    public void applyMentoringWithPayment(User user, MentoringApplyRequest request) {
        Payment payment = paymentGetService.byUserAndOrderId(user, request.orderId());
        if (mentoringGetService.byPayment(payment).isPresent())
            throw new MentoringPresentException();
        try {
            String[] dates = request.date().split(",");
            if (dates.length != 3)
                throw new MentoringDateException();
            Senior senior = payment.getSalary().getSenior();

            Mentoring mentoring = MentoringMapper.mapToMentoring(user, senior, payment, request);
            mentoringSaveService.save(mentoring);
        } catch (Exception ex) {
            paymentManageUseCase.refundPay(user, payment.getOrderId());
        }
    }
    public void updateCancel(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPay(user, payment.getOrderId());
        mentoringUpdateService.updateStatus(mentoring, CANCEL);
    }

    public void updateDone(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (mentoring.getStatus() != EXPECTED)
            throw new MentoringNotExpectedException();
        Salary salary = salaryGetService.bySenior(mentoring.getSenior());
        salaryUpdateService.updateTotalAmount(salary);
        mentoringUpdateService.updateStatus(mentoring, DONE);
    }

    public void updateRefuse(User user, Long mentoringId, MentoringRefuseRequest request) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(senior, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        Refuse refuse = RefuseMapper.mapToRefuse(mentoring, request);
        refuseSaveService.save(refuse);
        mentoringUpdateService.updateStatus(mentoring, REFUSE);
    }

    public Boolean updateExpected(User user, Long mentoringId, MentoringDateRequest dateRequest) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(senior, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        mentoringUpdateService.updateDate(mentoring, dateRequest.date());
        mentoringUpdateService.updateStatus(mentoring, EXPECTED);
        Optional<Account> account = accountGetService.bySenior(senior);
        return account.isPresent();
    }

    public void delete(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        mentoringDeleteService.deleteMentoring(mentoring);
    }

    @Scheduled(cron = "0 59 23 * * *", zone = "Asia/Seoul")
    public void updateAutoCancel() {
        LocalDateTime now = LocalDateTime.now()
                .toLocalDate()
                .atStartOfDay();
        List<Mentoring> waitingMentorings = mentoringGetService.byStatusAndCreatedAt(WAITING, now);
        waitingMentorings.forEach(mentoring -> {
            mentoringUpdateService.updateStatus(mentoring, CANCEL);
            Refuse refuse = RefuseMapper.mapToRefuse(mentoring);
            refuseSaveService.save(refuse);
            paymentManageUseCase.refundPay(mentoring.getUser(), mentoring.getPayment().getOrderId());
            //TODO : 알림 보내거나 나머지 작업
        });
    }

    @Scheduled(cron = "0 59 23 * * *", zone = "Asia/Seoul")
    public void updateAutoDone() {
        List<Mentoring> expectedMentorings = mentoringGetService.byStatus(EXPECTED);
        expectedMentorings.stream()
                .filter(Mentoring::checkAutoDone)
                .forEach(mentoring -> {
                    mentoringUpdateService.updateStatus(mentoring, DONE);
                    Senior senior = mentoring.getSenior();
                    Salary salary = salaryGetService.bySenior(senior);
                    salaryUpdateService.updateTotalAmount(salary);
                    //TODO : 알림 보내거나 나머지 작업
                });
    }
}
