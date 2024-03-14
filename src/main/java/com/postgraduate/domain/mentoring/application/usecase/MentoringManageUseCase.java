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
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
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
import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MentoringManageUseCase {
    private final MentoringRenewalUseCase mentoringRenewalUseCase;
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
    private final SlackErrorMessage slackErrorMessage;

    @Transactional
    public boolean applyMentoringWithPayment(User user, MentoringApplyRequest request) {
        Payment payment = null;
        try {
            payment = paymentGetService.byUserAndOrderId(user, request.orderId());
            mentoringGetService.checkByPayment(payment);
            String[] dates = request.date().split(",");
            if (dates.length != 3)
                throw new MentoringDateException();
            Senior senior = payment.getSenior();
            Mentoring mentoring = MentoringMapper.mapToMentoring(user, senior, payment, request);
            mentoringSaveService.save(mentoring);
            return true;
        } catch (PaymentNotFoundException ex) {
            log.error("결제건을 찾을 수 없습니다.");
            return false;
        } catch (Exception ex) {
            paymentManageUseCase.refundPayByUser(user, payment.getOrderId());
            return false;
        }
    }

    @Transactional
    public void updateCancel(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayByUser(user, payment.getOrderId());
        mentoringUpdateService.updateStatus(mentoring, CANCEL);
    }


    @Transactional
    public void updateDone(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (mentoring.getStatus() != EXPECTED)
            throw new MentoringNotExpectedException();
        Salary salary = salaryGetService.bySenior(mentoring.getSenior());
        salaryUpdateService.updateTotalAmount(salary);
        mentoringUpdateService.updateDone(mentoring, salary);
    }


    @Transactional
    public void updateRefuse(User user, Long mentoringId, MentoringRefuseRequest request) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(senior, mentoringId);
        if (mentoring.getStatus() != WAITING)
            throw new MentoringNotWaitingException();
        Refuse refuse = RefuseMapper.mapToRefuse(mentoring, request);
        refuseSaveService.save(refuse);
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayBySenior(senior, payment.getOrderId());
        mentoringUpdateService.updateStatus(mentoring, REFUSE);
    }


    @Transactional
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


    @Transactional
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
        waitingMentorings.forEach(mentoringRenewalUseCase::updateCancelWithAuto);
        //TODO : 알림 보내거나 나머지 작업
    }

    @Scheduled(cron = "0 59 23 * * *", zone = "Asia/Seoul")
    public void updateAutoDone() {
        List<Mentoring> expectedMentorings = mentoringGetService.byStatus(EXPECTED);
        expectedMentorings.stream()
                .filter(mentoring -> {
                    try {
                        return mentoring.checkAutoDone();
                    } catch (Exception ex) {
                        slackErrorMessage.sendSlackError(mentoring, ex);
                        return false;
                    }
                })
                .forEach(mentoringRenewalUseCase::updateDoneWithAuto);
        //TODO : 알림 보내거나 나머지 작업
    }
}
