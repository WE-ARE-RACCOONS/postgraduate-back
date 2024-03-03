package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.refuse.application.mapper.RefuseMapper;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.CANCEL;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MentoringRenewalUseCase {
    private final MentoringGetService mentoringGetService;
    private final MentoringUpdateService mentoringUpdateService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final RefuseSaveService refuseSaveService;
    private final SalaryUpdateService salaryUpdateService;
    private final SalaryGetService salaryGetService;
    private final SlackErrorMessage slackErrorMessage;

    public void updateCancelWithAuto(Mentoring mentoring) {
        try {
            paymentManageUseCase.refundPayByUser(mentoring.getUser(), mentoring.getPayment().getOrderId());
            Mentoring cancelMentoring = mentoringGetService.byMentoringIdWithLazy(mentoring.getMentoringId());
            mentoringUpdateService.updateStatus(cancelMentoring, CANCEL);
            Refuse refuse = RefuseMapper.mapToRefuse(mentoring);
            refuseSaveService.save(refuse);
            log.info("mentoringId : {} 자동 취소", mentoring.getMentoringId());
        } catch (Exception ex) {
            log.error("mentoringId : {} 자동 취소 실패", mentoring.getMentoringId());
            log.error(ex.getMessage());
            slackErrorMessage.sendSlackError(mentoring, ex);
        }
    }

    public void updateDoneWithAuto(Mentoring mentoring) {
        try {
            Mentoring doneMentoring = mentoringGetService.byMentoringIdWithLazy(mentoring.getMentoringId());
            mentoringUpdateService.updateStatus(doneMentoring, DONE);
            Senior senior = mentoring.getSenior();
            Salary salary = salaryGetService.bySenior(senior);
            salaryUpdateService.updateTotalAmount(salary);
            log.info("mentoringId : {} 자동 완료", mentoring.getMentoringId());
        } catch (Exception ex) {
            slackErrorMessage.sendSlackError(mentoring, ex);
            log.error("mentoringId : {} 자동 완료 실패", mentoring.getMentoringId());
            log.error(ex.getMessage());
        }
    }
}
