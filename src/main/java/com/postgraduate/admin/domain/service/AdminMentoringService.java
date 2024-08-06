package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminMentoringRepository;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.CANCEL;

@RequiredArgsConstructor
@Service
public class AdminMentoringService {
    private final AdminMentoringRepository adminMentoringRepository;

    public List<Mentoring> allBySeniorId(Long seniorId) {
        return adminMentoringRepository.findAllBySeniorId(seniorId);
    }

    public List<Mentoring> allByUserId(Long userId) {
        return adminMentoringRepository.findAllByUserId(userId);
    }

    public Mentoring byPaymentId(Long paymentId) {
        return adminMentoringRepository.findByPaymentId(paymentId)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public Mentoring updateCancelWithPaymentId(Long paymentId) {
        Mentoring mentoring = byPaymentId(paymentId);
        mentoring.updateStatus(CANCEL);
        return mentoring;
    }

    public Mentoring updateCancelWithMentoringId(Long mentoringId) {
        Mentoring mentoring = adminMentoringRepository.findByMentoringId(mentoringId)
                .orElseThrow(MentoringNotFoundException::new);
        mentoring.updateStatus(CANCEL);
        return mentoring;
    }
}
