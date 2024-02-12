package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;
import com.postgraduate.domain.admin.application.dto.res.PaymentManageResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentManageByAdminUseCase {
    private final MentoringGetService mentoringGetService;

    public PaymentManageResponse getPayments(Integer page, String search) {
        Page<Mentoring> mentorings = mentoringGetService.all(page, search);
        List<PaymentInfo> paymentInfos = mentorings.map(AdminMapper::mapToPaymentInfo).toList();
        long totalElements = mentorings.getTotalElements();
        int totalPages = mentorings.getTotalPages();
        return new PaymentManageResponse(paymentInfos, totalElements, totalPages);
    }
}
