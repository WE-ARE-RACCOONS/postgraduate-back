package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringGetService {
    private static final int ADMIN_PAGE_SIZE = 15;

    private final MentoringRepository mentoringRepository;

    public void byPayment(Payment payment) {
        mentoringRepository.findByPayment(payment)
                .ifPresent(mentoring -> {
                    throw new MentoringPresentException();
                });
    }

    public List<Mentoring> byUser(User user, Status status) {
        return mentoringRepository.findAllByUserAndStatus(user, status);
    }

    public List<Mentoring> bySenior(Senior senior, Status status) {
        return mentoringRepository.findAllBySeniorAndStatus(senior, status);
    }

    public List<Mentoring> bySenior(Senior senior) {
        return mentoringRepository.findAllBySeniorAndDone(senior);
    }

    public Mentoring byMentoringId(Long mentoringId) {
        return mentoringRepository.findByMentoringId(mentoringId)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public List<Mentoring> byStatusAndCreatedAt(Status status, LocalDateTime now) {
        return mentoringRepository.findAllByStatusAndCreatedAtIsBefore(status, now);
    }

    public List<Mentoring> byStatus(Status status) {
        return mentoringRepository.findAllByStatus(status);
    }

    public List<Mentoring> byUserId(Long userId) {
        return mentoringRepository.findAllByUserId(userId);
    }

    public List<Mentoring> bySeniorId(Long seniorId) {
        return mentoringRepository.findAllBySeniorId(seniorId);
    }

    public List<Mentoring> bySeniorAndSalaryStatus(Senior senior, Boolean status) {
        return mentoringRepository.findAllBySeniorAndSalaryStatus(senior, status);
    }

    public Page<Mentoring> all(Integer page, String search) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return mentoringRepository.findAllBySearchPayment(search, pageable);
    }
}
