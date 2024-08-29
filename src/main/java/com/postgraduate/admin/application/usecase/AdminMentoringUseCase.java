package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.MentoringManageResponse;
import com.postgraduate.admin.application.dto.res.UserMentoringInfo;
import com.postgraduate.admin.application.dto.res.MentoringInfo;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.*;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.DONE;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminMentoringUseCase {
    private final PaymentManageUseCase paymentManageUseCase;
    private final AdminSeniorService adminSeniorService;
    private final AdminMentoringService adminMentoringService;
    private final AdminUserService adminUserService;
    private final AdminSalaryService adminSalaryService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public MentoringManageResponse seniorMentorings(Long seniorId) {
        Senior senior = adminSeniorService.bySeniorId(seniorId);
        List<Mentoring> mentorings = adminMentoringService.allBySeniorId(seniorId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(adminMapper::mapToMentoringInfoWithSenior)
                .toList();
        UserMentoringInfo seniorInfo = adminMapper.mapToUserMentoringInfo(senior);
        return new MentoringManageResponse(mentoringInfos, seniorInfo);
    }

    @Transactional(readOnly = true)
    public MentoringManageResponse userMentoringInfos(Long userId) {
        User user = adminUserService.userByUserId(userId);
        List<Mentoring> mentorings = adminMentoringService.allByUserId(userId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(adminMapper::mapToMentoringInfoWithUser)
                .toList();
        UserMentoringInfo userInfo = adminMapper.mapToUserMentoringInfo(user);
        return new MentoringManageResponse(mentoringInfos, userInfo);
    }

    public void refundMentoring(User user, Long mentoringId) {
        Mentoring mentoring = adminMentoringService.updateCancelWithMentoringId(mentoringId);
        Payment payment = mentoring.getPayment();
        paymentManageUseCase.refundPayByAdmin(user, payment.getPaymentId());
        if (mentoring.getStatus() == DONE)
            adminSalaryService.minusTotalAmount(mentoring);
    }
}
