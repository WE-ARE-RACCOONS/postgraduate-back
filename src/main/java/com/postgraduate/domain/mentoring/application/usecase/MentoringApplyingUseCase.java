package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.senior.account.domain.entity.Account;
import com.postgraduate.domain.senior.account.domain.service.AccountGetService;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.ApplyingResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MentoringApplyingUseCase {
    private final PaymentGetService paymentGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final MentoringGetService mentoringGetService;
    private final MentoringSaveService mentoringSaveService;
    private final AccountGetService accountGetService;
    private final MentoringMapper mentoringMapper;
    private final BizppurioSeniorMessage bizppurioSeniorMessage;
    private final BizppurioJuniorMessage bizppurioJuniorMessage;

    public ApplyingResponse applyMentoringWithPayment(User user, MentoringApplyRequest request) {
        Payment payment = paymentGetService.byUserAndOrderId(user, request.orderId());
        mentoringGetService.checkByPayment(payment);
        String[] dates = request.date().split(",");
        if (dates.length != 3)
            throw new MentoringDateException();
        Senior senior = payment.getSenior();
        Mentoring mentoring = mentoringMapper.mapToMentoring(user, senior, payment, request);
        mentoringSaveService.saveMentoring(mentoring);
        seniorUpdateService.plusMentoring(senior);
        Optional<Account> account = accountGetService.bySenior(senior);
        sendMessage(user, senior);
        return new ApplyingResponse(account.isPresent());
    }

    private void sendMessage(User user, Senior senior) {
        bizppurioJuniorMessage.mentoringApply(user);
        bizppurioSeniorMessage.mentoringApply(senior.getUser());
    }
}
