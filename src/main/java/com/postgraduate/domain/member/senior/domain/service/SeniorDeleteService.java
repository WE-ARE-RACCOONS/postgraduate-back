package com.postgraduate.domain.member.senior.domain.service;

import com.postgraduate.domain.member.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeniorDeleteService {
    private final SeniorRepository seniorRepository;
    private final PaymentRepository paymentRepository;
    private final MentoringRepository mentoringRepository;
    private final SalaryRepository salaryRepository;

    public void deleteSenior(User user) {
        Senior senior = seniorRepository.findByUserAndUser_IsDelete(user, true)
                .orElseThrow(UserNotFoundException::new);
        //account 삭제
        seniorRepository.deleteAccount(senior);
        //available 삭제
        seniorRepository.deleteAvailableBySenior(senior);
        //mentoring senior null
        mentoringRepository.findAllBySenior(senior)
                .stream()
                .forEach(Mentoring::updateSeniorDelete);
        //salary senior null
        salaryRepository.findAllBySenior(senior)
                .stream()
                .forEach(Salary::updateSeniorDelete);
        //payment senior null
        paymentRepository.findAllBySenior(senior)
                .stream()
                .forEach(Payment::updateSeniorDelete);
        //senior 삭제
        seniorRepository.deleteById(senior.getSeniorId());
    }

    public void deleteAvailable(Senior senior) {
        seniorRepository.deleteAvailableBySenior(senior);
    }
}
