package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringGetService {
    private final MentoringRepository mentoringRepository;

    public List<Mentoring> byUser(User user, Status status) {
        return mentoringRepository.findAllByUserAndStatus(user, status);
    }

    public List<Mentoring> bySenior(Senior senior, Status status) {
        return mentoringRepository.findAllBySeniorAndStatus(senior, status);
    }

    public List<DoneSeniorMentoringInfo> bySenior(Senior senior) {
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
}
