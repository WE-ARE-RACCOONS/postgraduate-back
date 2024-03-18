package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.admin.application.dto.MentoringInfo;
import com.postgraduate.domain.admin.application.dto.UserMentoringInfo;
import com.postgraduate.domain.admin.application.dto.res.MentoringManageResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
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

import static com.postgraduate.domain.admin.application.mapper.AdminMapper.mapToMentoringWithPaymentResponse;
import static com.postgraduate.domain.admin.application.mapper.AdminMapper.mapToUserMentoringInfo;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.CANCEL;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminMentoringUseCase {
    private final MentoringGetService mentoringGetService;
    private final MentoringUpdateService mentoringUpdateService;
    private final SeniorGetService seniorGetService;
    private final UserGetService userGetService;
    private final PaymentGetService paymentGetService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;

    public MentoringManageResponse seniorMentorings(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        List<Mentoring> mentorings = mentoringGetService.bySeniorId(seniorId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        UserMentoringInfo seniorInfo = mapToUserMentoringInfo(senior);
        return new MentoringManageResponse(mentoringInfos, seniorInfo);
    }

    public MentoringManageResponse userMentoringInfos(Long userId) {
        User user = userGetService.byUserId(userId);
        List<Mentoring> mentorings = mentoringGetService.byUserId(userId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        UserMentoringInfo userInfo = mapToUserMentoringInfo(user);
        return new MentoringManageResponse(mentoringInfos, userInfo);
    }

    public MentoringWithPaymentResponse paymentMentoringInfo(Long paymentId) {
        Payment payment = paymentGetService.byId(paymentId);
        Mentoring mentoring = mentoringGetService.byPayment(payment);
        return mapToMentoringWithPaymentResponse(mentoring);
    }

    public void refundMentoring(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayByAdmin(user, payment.getPaymentId());
        if (mentoring.getStatus() == DONE) {
            Senior senior = mentoring.getSenior();
            Salary salary = salaryGetService.bySenior(senior);
            salaryUpdateService.minusTotalAmount(salary);
        }
        mentoringUpdateService.updateCancel(mentoring);
    }
}
