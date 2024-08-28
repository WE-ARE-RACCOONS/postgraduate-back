package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.admin.application.dto.res.PaymentInfo;
import com.postgraduate.admin.application.dto.res.PaymentWithMentoringQuery;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminMentoringService;
import com.postgraduate.admin.domain.service.AdminPaymentService;
import com.postgraduate.admin.domain.service.AdminSalaryService;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.DONE;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPaymentUseCase {
    private final PaymentManageUseCase paymentManageUseCase;
    private final AdminSalaryService adminSalaryService;
    private final AdminPaymentService adminPaymentService;
    private final AdminMentoringService adminMentoringService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public List<PaymentInfo> paymentInfos() {
        List<PaymentWithMentoringQuery> all = adminPaymentService.allPayments();
        return all.stream()
                .map(pm -> {
                    if (pm.mentoring().isEmpty())
                        return adminMapper.mapToPaymentInfo(pm.payment());
                    return adminMapper.mapToPaymentInfo(pm.payment(), pm.mentoring().get());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public MentoringWithPaymentResponse paymentMentoringInfo(Long paymentId) {
        Mentoring mentoring = adminMentoringService.byPaymentId(paymentId);
        return adminMapper.mapToMentoringWithPaymentResponse(mentoring);
    }

    public void refundPayment(User user, Long paymentId) {
        paymentManageUseCase.refundPayByAdmin(user, paymentId);
        try {
            Mentoring mentoring = adminMentoringService.updateCancelWithPaymentId(paymentId);
            if (mentoring.getStatus() == DONE)
                adminSalaryService.minusTotalAmount(mentoring);
        } catch (Exception ex) {
            // todo: 환불 이후 예외 발생시 처리
        }
    }
}
