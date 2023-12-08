package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.MentoringInfo;
import com.postgraduate.domain.admin.application.dto.res.MentoringManageResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageByAdminUseCase {
    private final MentoringGetService mentoringGetService;
    private final PaymentGetService paymentGetService;
    public MentoringManageResponse getUserMentorings(Long userId) {
        List<Mentoring> mentorings = mentoringGetService.byUserId(userId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        return new MentoringManageResponse(mentoringInfos);
    }

    public MentoringManageResponse getSeniorMentorings(Long seniorId) {
        List<Mentoring> mentorings = mentoringGetService.bySeniorId(seniorId);
        List<MentoringInfo> mentoringInfos = mentorings.stream()
                .map(AdminMapper::mapToMentoringInfo)
                .toList();
        return new MentoringManageResponse(mentoringInfos);
    }

    public MentoringWithPaymentResponse getMentoringWithPayment(Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        Payment payment = paymentGetService.byMentoring(mentoring);
        return AdminMapper.mapToMentoringWithPaymentResponse(payment, mentoring);
    }
}
