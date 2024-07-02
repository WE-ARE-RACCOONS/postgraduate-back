package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.res.ApplyingResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.util.DateUtils.mentoringDateToTime;
import static com.postgraduate.domain.mentoring.util.DateUtils.stringToLocalDateTime;
import static com.postgraduate.domain.refuse.application.mapper.RefuseMapper.mapToRefuse;
import static java.time.LocalDateTime.now;

@Service
@Slf4j
@RequiredArgsConstructor
public class MentoringManageUseCase {
    private final MentoringUpdateService mentoringUpdateService;
    private final MentoringGetService mentoringGetService;
    private final RefuseSaveService refuseSaveService;
    private final AccountGetService accountGetService;
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final MentoringApplyingUseCase mentoringApplyingUseCase;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;
    private final BizppurioSeniorMessage bizppurioSeniorMessage;

    public ApplyingResponse applyMentoring(User user, MentoringApplyRequest request) {
        try {
            return mentoringApplyingUseCase.applyMentoringWithPayment(user, request);
        } catch (PaymentNotFoundException ex) {
            log.error("결제건을 찾을 수 없습니다.");
            throw ex;
        } catch (MentoringPresentException ex) {
            log.error("이미 신청된 결제건 입니다.");
            throw ex;
        } catch (Exception ex) {
            paymentManageUseCase.refundPayByUser(user, request.orderId());
            throw ex;
        }
    }

    @Transactional
    public void updateCancel(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byIdAndUserAndWaiting(mentoringId, user);
        Senior senior = mentoring.getSenior();
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayByUser(user, payment.getOrderId());
        mentoringUpdateService.updateCancel(mentoring);
        seniorUpdateService.minusMentoring(senior);
        bizppurioSeniorMessage.mentoringRefund(senior.getUser());
    }


    @Transactional
    public void updateDone(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byIdAndUserAndExpected(mentoringId, user);
        Salary salary = salaryGetService.bySenior(mentoring.getSenior());
        salaryUpdateService.plusTotalAmount(salary, mentoring.calculateForSenior());
        mentoringUpdateService.updateDone(mentoring, salary);
    }


    @Transactional
    public void updateRefuse(User user, Long mentoringId, MentoringRefuseRequest request) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior);
        Refuse refuse = mapToRefuse(mentoring, request);
        refuseSaveService.save(refuse);
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayBySenior(senior, payment.getOrderId());
        mentoringUpdateService.updateRefuse(mentoring);
        seniorUpdateService.minusMentoring(senior);
        bizppurioJuniorMessage.mentoringRefuse(mentoring.getUser());
    }


    @Transactional
    public Boolean updateExpected(User user, Long mentoringId, MentoringDateRequest dateRequest) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior);
        mentoringUpdateService.updateExpected(mentoring, dateRequest.date());
        Optional<Account> account = accountGetService.bySenior(senior);
        String time = mentoringDateToTime(dateRequest.date());
        bizppurioSeniorMessage.mentoringAccept(senior, time);
        bizppurioJuniorMessage.mentoringAccept(mentoring.getUser(), senior, time);
        return account.isPresent();
    }

    @Scheduled(fixedDelay = 1000*60*10)
    @Transactional
    public void sendFinishMessage() {
        List<Mentoring> expectedMentorings = mentoringGetService.byForMessage();
        expectedMentorings.stream()
                .filter(mentoring -> {
                    LocalDateTime mentoringDate = stringToLocalDateTime(mentoring.getDate());
                    LocalDateTime finishTime = mentoringDate.plusMinutes(mentoring.getTerm()).plusMinutes(10);
                    return (finishTime.isBefore(now()) && finishTime.isAfter(now().minusMinutes(10)));
                })
                .forEach(mentoring -> {
                    bizppurioJuniorMessage.mentoringFinish(mentoring.getUser());
                    bizppurioSeniorMessage.mentoringFinish(mentoring.getSenior().getUser());
                });
    }
}
