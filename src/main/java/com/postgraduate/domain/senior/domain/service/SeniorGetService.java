package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.senior.exception.NoneSeniorException;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;

@Service
@RequiredArgsConstructor
public class SeniorGetService {
    private final SeniorRepository seniorRepository;

    public Senior byUser(User user) {
        return seniorRepository.findByUser(user).orElseThrow(NoneSeniorException::new);
    }

    public Senior bySeniorId(Long seniorId) {
        return seniorRepository.findById(seniorId).orElseThrow(NoneSeniorException::new);
    }

    public Senior bySeniorIdWithCertification(Long seniorId) {
        return seniorRepository.findBySeniorIdAndProfileNotNullAndStatus(seniorId, APPROVE).orElseThrow(NoneSeniorException::new);
    }

    public List<Senior> byStatus(Status status) {
        return seniorRepository.findAllByStatus(status).orElse(new ArrayList<>());
    }
}
