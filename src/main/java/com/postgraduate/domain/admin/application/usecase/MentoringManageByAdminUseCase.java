package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.MentoringInfo;
import com.postgraduate.domain.admin.application.dto.UserMentoringInfo;
import com.postgraduate.domain.admin.application.dto.res.MentoringManageResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.domain.service.PaymentUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.payment.domain.entity.constant.Status.CANCEL;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageByAdminUseCase {
    private final MentoringGetService mentoringGetService;
    private final MentoringUpdateService mentoringUpdateService;
    private final UserGetService userGetService;
    private final SeniorGetService seniorGetService;
    private final PaymentGetService paymentGetService;
    private final PaymentUpdateService paymentUpdateService;

    public MentoringManageResponse getUserMentorings(Long userId) {
        List<Mentoring> mentorings = mentoringGetService.byUserId(userId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        User user = userGetService.getUser(userId);
        UserMentoringInfo userMentoringInfo = AdminMapper.mapToUserMentoringInfo(user);
        return new MentoringManageResponse(mentoringInfos, userMentoringInfo);
    }

    public MentoringManageResponse getSeniorMentorings(Long seniorId) {
        List<Mentoring> mentorings = mentoringGetService.bySeniorId(seniorId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        Senior senior = seniorGetService.bySeniorId(seniorId);
        UserMentoringInfo userMentoringInfo = AdminMapper.mapToUserMentoringInfo(senior);
        return new MentoringManageResponse(mentoringInfos, userMentoringInfo);
    }

    public MentoringWithPaymentResponse getMentoringWithPayment(Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        Payment payment = paymentGetService.byMentoring(mentoring);
        return AdminMapper.mapToMentoringWithPaymentResponse(payment, mentoring);
    }

    public void cancelMentoring(Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        mentoringUpdateService.updateStatus(mentoring, Status.CANCEL);
        Payment payment = paymentGetService.byMentoring(mentoring);
        paymentUpdateService.updateStatus(payment, CANCEL);
    }
}
