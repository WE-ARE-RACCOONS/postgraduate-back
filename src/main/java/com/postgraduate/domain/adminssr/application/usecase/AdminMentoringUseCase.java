package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.adminssr.application.dto.res.UserMentoringInfo;
import com.postgraduate.domain.adminssr.application.dto.res.MentoringInfo;
import com.postgraduate.domain.adminssr.application.dto.res.MentoringManageResponse;
import com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper.*;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminMentoringUseCase {
    private final MentoringGetService mentoringGetService;
    private final MentoringUpdateService mentoringUpdateService;
    private final SeniorGetService seniorGetService;
    private final UserGetService userGetService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;

    @Transactional(readOnly = true)
    public MentoringManageResponse seniorMentorings(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        List<Mentoring> mentorings = mentoringGetService.bySeniorId(seniorId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminSsrMapper::mapToMentoringInfoWithSenior)
                .toList();
        UserMentoringInfo seniorInfo = mapToUserMentoringInfo(senior);
        return new MentoringManageResponse(mentoringInfos, seniorInfo);
    }

    @Transactional(readOnly = true)
    public MentoringManageResponse userMentoringInfos(Long userId) {
        User user = userGetService.byUserId(userId);
        List<Mentoring> mentorings = mentoringGetService.byUserId(userId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminSsrMapper::mapToMentoringInfoWithUser)
                .toList();
        UserMentoringInfo userInfo = mapToUserMentoringInfo(user);
        return new MentoringManageResponse(mentoringInfos, userInfo);
    }

    public void refundMentoring(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayByAdmin(user, payment.getPaymentId());
        if (mentoring.getStatus() == DONE) {
            Senior senior = mentoring.getSenior();
            Salary salary = salaryGetService.bySenior(senior);
            salaryUpdateService.minusTotalAmount(salary, mentoring.calculateForSenior());
        }
        mentoringUpdateService.updateCancel(mentoring);
    }
}
