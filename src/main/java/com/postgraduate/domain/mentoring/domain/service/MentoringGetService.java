package com.postgraduate.domain.mentoring.domain.service;

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

import static java.lang.Boolean.FALSE;

@Service
@RequiredArgsConstructor
public class MentoringGetService {
    private final MentoringRepository mentoringRepository;

    public List<Mentoring> mentoringByUser(User user, Status status) {
        return mentoringRepository.findAllByUserAndStatus(user, status);
    }

    public List<Mentoring> mentoringBySenior(Senior senior, Status status) {
        return mentoringRepository.findAllBySeniorAndStatus(senior, status);
    }

    public Mentoring byMentoringId(Long mentoringId) {
        return mentoringRepository.findByMentoringIdAndUser_IsDeleteAndSenior_User_IsDelete(mentoringId, FALSE, FALSE)
                .orElseThrow(MentoringNotFoundException::new);
    }

    public List<Mentoring> byStatusAndCreatedAt(Status status, LocalDateTime now) {
        return mentoringRepository.findAllByStatusAndCreatedAtIsBefore(status, now);
    }

    public List<Mentoring> byUserId(Long userId) {
        return mentoringRepository.findAllByUser_UserId(userId);
    }

    public List<Mentoring> bySeniorId(Long seniorId) {
        return mentoringRepository.findAllBySenior_SeniorId(seniorId);
    }
}
