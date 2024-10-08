package com.postgraduate.batch.cancel;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CancelMentoringProcessor implements ItemProcessor<Mentoring, CancelMentoring> {
    @Override
    public CancelMentoring process(Mentoring mentoring) throws Exception {
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        Payment payment = mentoring.getPayment();
        return new CancelMentoring(mentoring.getMentoringId(), user.getUserId(), senior.getSeniorId(), payment.getPaymentId());
    }
}
