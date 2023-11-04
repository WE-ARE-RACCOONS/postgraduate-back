package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeniorGetService {
    private final SeniorRepository seniorRepository;

    public Senior byUser(User user) {
        return seniorRepository.findByUser(user).orElseThrow(/**예외 처리**/);
    }
}
