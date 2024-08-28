package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class MentoringGetService {

    private final MentoringRepository mentoringRepository;

    public void checkByPayment(Payment payment) {
        mentoringRepository.findByPayment(payment)
                .ifPresent(mentoring -> {
                    throw new MentoringPresentException();
                });
    }

    public Mentoring byPaymentWithNull(Payment payment) {
        return mentoringRepository.findByPayment(payment)
                .orElse(null);
    }

    public Mentoring byPayment(Payment payment) {
        return mentoringRepository.findByPayment(payment)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public List<Mentoring> byUserWaiting(User user) {
        return mentoringRepository.findAllByUserAndStatus(user, WAITING);
    }

    public List<Mentoring> byUserExpected(User user) {
        return mentoringRepository.findAllByUserAndStatus(user, EXPECTED);
    }

    public List<Mentoring> byUserDone(User user) {
        return mentoringRepository.findAllByUserAndStatus(user, DONE);
    }

    public List<Mentoring> bySeniorWaiting(Senior senior) {
        return mentoringRepository.findAllBySeniorAndStatus(senior, WAITING);
    }

    public List<Mentoring> bySeniorExpected(Senior senior) {
        return mentoringRepository.findAllBySeniorAndStatus(senior, EXPECTED);
    }

    public List<Mentoring> bySeniorDone(Senior senior) {
        return mentoringRepository.findAllBySeniorAndStatus(senior, DONE);
    }

    public Mentoring byIdAndUserForDetails(Long mentoringId, User user) {
        return mentoringRepository.findByMentoringIdAndUserForDetails(mentoringId, user)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public Mentoring byIdAndSeniorForDetails(Long mentoringId, Senior senior) {
        return mentoringRepository.findByMentoringIdAndSeniorForDetails(mentoringId, senior)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public Mentoring byIdAndUserAndWaiting(Long mentoringId, User user) {
        return mentoringRepository.findByMentoringIdAndUserAndStatus(mentoringId, user, WAITING)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public Mentoring byIdAndUserAndExpected(Long mentoringId, User user) {
        return mentoringRepository.findByMentoringIdAndUserAndStatus(mentoringId, user, EXPECTED)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public Mentoring byIdAndSeniorAndWaiting(Long mentoringId, Senior senior) {
        return mentoringRepository.findByMentoringIdAndSeniorAndStatus(mentoringId, senior, WAITING)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public Mentoring byIdAndSeniorAndExpected(Long mentoringId, Senior senior) {
        return mentoringRepository.findByMentoringIdAndSeniorAndStatus(mentoringId, senior, EXPECTED)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public List<Mentoring> bySeniorAndSalaryTrue(Senior senior) {
        return mentoringRepository.findAllBySeniorAndSalaryStatus(senior, TRUE);
    }

    public List<Mentoring> bySeniorAndSalaryFalse(Senior senior) {
        return mentoringRepository.findAllBySeniorAndSalaryStatus(senior, FALSE);
    }

    public List<Mentoring> byForMessage() {
        return mentoringRepository.findAllForMessage();
    }
}
