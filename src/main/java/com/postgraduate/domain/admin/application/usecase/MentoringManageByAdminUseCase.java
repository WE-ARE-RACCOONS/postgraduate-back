package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.res.MentoringResponse;
import com.postgraduate.domain.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
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
    public List<MentoringResponse> getUserMentorings(Long userId) {
        List<Mentoring> mentorings = mentoringGetService.byUserId(userId);
        return mentorings.stream().map(AdminMapper::mapToMentoringResponse).toList();
    }

    public List<MentoringResponse> getSeniorMentorings(Long seniorId) {
        List<Mentoring> mentorings = mentoringGetService.bySeniorId(seniorId);
        return mentorings.stream().map(AdminMapper::mapToMentoringResponse).toList();
    }

    public MentoringWithPaymentResponse getMentoringWithPayment(Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        if (!paymentGetService.existsByMentoring(mentoring)) {
            throw new PaymentNotFoundException();
        }
        Payment payment = paymentGetService.byMentoring(mentoring);
        return AdminMapper.mapToMentoringWithPaymentResponse(payment.getPaymentId(), mentoring);
    }
}
